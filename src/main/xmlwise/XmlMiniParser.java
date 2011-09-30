package xmlwise;

import java.io.*;

/**
 * @author Christoffer Lerno
 */
public class XmlMiniParser
{
    private PushbackReader m_stream;
    private char m_lastChar;
    private int m_row;
    private int m_elementRow;

    public XmlMiniParser()
    {
    }

    private void skipDoctype() throws IOException
    {
        space();
        while (readOptional("<!DOCTYPE"))
        {
            readUntil('>');
        }
    }

    public XmlElement parse(InputStream stream) throws XmlParseException
    {
        try
        {
            m_stream = new PushbackReader(new InputStreamReader(stream));
            m_row = 1;
            read("<?xml", "Missing topmost xml tag");
            readUntil('>');
            skipDoctype();
            return readXml();
        }
        catch (EOFException e)
        {
            throw new XmlParseException("XML ended unexpectedly");
        }
        catch (IOException e)
        {
            throw new XmlParseException("IO exception reading xml", e);
        }
    }

    private String readUntil(char c) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        while (c != read())
        {
            builder.append(m_lastChar);
        }
        pushback();
        return builder.toString();
    }

    private String readUntil(char c1, char c2) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        while (c1 != read() && c2 != m_lastChar)
        {
            builder.append(m_lastChar);
        }
        pushback();
        return builder.toString();
    }

    private XmlElement readXml() throws XmlParseException, IOException
    {
        read("<", "Cannot find top element of xml");
        return readElement();
    }

    private XmlElement readElement() throws IOException, XmlParseException
    {
        m_elementRow = m_row;
        XmlElement element = new XmlElement(readUntil(' ', '>'));
        if (m_lastChar == ' ')
        {
            readAttributes(element);
        }
        read(">", "Failed to find end of element " + element.getName() + " at row " + m_elementRow);
        space();
        
        return element;
    }

    private void readAttributes(XmlElement element) throws IOException, XmlParseException
    {
        while (true)
        {
            space();
            if (m_lastChar == '>') return;
            String attribute = readUntil(' ', '=');
            if (m_lastChar == ' ') space();
            read("=", "Malformed attribute to element " + element.getName());
            space();
            if (m_lastChar != '\'' || m_lastChar != '"') throw new XmlParseException("Malformed attribute " + attribute + " in element " + element.getName() + " at row " + m_elementRow);
            String attributeValue = readQuotedString(m_lastChar);
            element.setAttribute(attribute, attributeValue);
        }
    }

    private String readQuotedString(char lastChar) throws IOException, XmlParseException
    {
        StringBuilder builder = new StringBuilder();
        while (read() != lastChar)
        {
            switch (m_lastChar)
            {
                case '\\':
                    builder.append(read());
                    break;
                case '&':
                    builder.append(xmlEscape());
                    break;
                default:
                    builder.append(m_lastChar);
            }
        }
        return builder.toString();
    }

    private String xmlEscape() throws IOException, XmlParseException
    {
        String value = readUntil(';');
        if (value.equals("amp")) return "&";
        if (value.equals("lt")) return "<";
        if (value.equals("gt")) return ">";
        if (value.equals("quot")) return "\"";
        if (value.equals("apos")) return "'";
        throw new XmlParseException("Cannot interpret '&" + value + ";' at row " + m_row);
        //if (value.startsWith("#"))
    }

    private boolean readOptional(String string) throws IOException
    {
        try
        {
            read(string, "");
            return true;
        }
        catch (XmlParseException e)
        {
            return false;
        }
    }

    private void read(String expectedString, String orError) throws IOException, XmlParseException
    {
        for (char c : expectedString.toCharArray())
        {
            if (c != read()) throw new XmlParseException(orError + " at row " + m_row);
        }
    }

    private char read() throws IOException
    {
        int value = m_stream.read();
        if (value == -1) throw new EOFException();
        m_lastChar = (char) value;
        return m_lastChar;
    }
    private void pushback() throws IOException
    {
        m_stream.unread(m_lastChar);
    }

    private void space() throws IOException
    {
        char c = read();
        while (c == ' ' || c == '\t' || c == '\n' || c == '\r')
        {
            if (c == '\n') m_row++;
        }
        pushback();
    }
}
