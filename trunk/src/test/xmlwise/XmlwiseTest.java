package xmlwise;
/**
 * @author Christoffer Lerno 
 */

import junit.framework.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XmlwiseTest extends TestCase
{
	public void testCreateErronousDocument() throws Exception
	{
		Xmlwise.createDocument("<c></c>");
		try
		{
			Xmlwise.createDocument("<x>dks</y>");
			fail("Should fail");
		}
		catch (XmlParseException e)
		{
		}
	}

    public void testMiniParserVsNormalParser() throws Exception
    {
        String plist = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                      "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                      "<plist version=\"1.0\"><dict><key>pages</key><array><dict><key>uid</key><string>page1</string><key>widgets</key><array><dict><key>fullscreen</key><false/><key>content</key><string>0.13,0.27,0.72,0.34</string><key>uid</key><string>page1w01</string><key>showoverlay</key><false/><key>popup</key><false/><key>showicon</key><true/><key>hotspot</key><string>0.26,0.76,0.58,0.2</string><key>autostart</key><false/><key>kind</key><string>video</string><key>url</key><string>backstage_conv.mp4</string></dict></array><key>number</key><integer>1</integer></dict><dict><key>uid</key><string>page7</string><key>widgets</key><array><dict><key>fullscreen</key><true/><key>content</key><string>0.0,0.0,0.5,0.5</string><key>uid</key><string>page7w01</string><key>showoverlay</key><true/><key>popup</key><false/><key>showicon</key><false/><key>hotspot</key><string>0.21,0.45,0.57,0.08</string><key>autostart</key><false/><key>kind</key><string>link</string><key>url</key><string>http://www.elle.it</string></dict></array><key>number</key><integer>7</integer></dict><dict><key>uid</key><string>page8</string><key>widgets</key><array><dict><key>fullscreen</key><false/><key>content</key><string>0.05,0.07,0.91,0.55</string><key>uid</key><string>page8w01</string><key>showoverlay</key><true/><key>popup</key><true/><key>showicon</key><false/><key>hotspot</key><string>0.04,0.86,0.09,0.07</string><key>autostart</key><false/><key>kind</key><string>link</string><key>url</key><string>http://www.hachettepubblicita.it</string></dict></array><key>number</key><integer>8</integer></dict><dict><key>uid</key><string>page11</string><key>widgets</key><array><dict><key>fullscreen</key><false/><key>content</key><string>0.0,0.0,0.5,0.5</string><key>uid</key><string>page11w01</string><key>showoverlay</key><false/><key>popup</key><false/><key>showicon</key><true/><key>hotspot</key><string>0.44,0.27,0.48,0.36</string><key>autostart</key><false/><key>kind</key><string>audio</string><key>url</key><string>06-Eh...già.mp3</string></dict></array><key>number</key><integer>11</integer></dict><dict><key>uid</key><string>page13</string><key>widgets</key><array><dict><key>fullscreen</key><false/><key>content</key><string>0.19,0.03,0.64,0.64</string><key>uid</key><string>page13w01</string><key>showoverlay</key><false/><key>popup</key><true/><key>showicon</key><true/><key>hotspot</key><string>0.51,0.87,0.16,0.05</string><key>autostart</key><false/><key>kind</key><string>pdf</string><key>url</key><string>PROVA_MM_01m01.pdf</string></dict><dict><key>fullscreen</key><false/><key>content</key><string>0.0,0.0,0.5,0.5</string><key>uid</key><string>page13w11</string><key>showoverlay</key><false/><key>popup</key><true/><key>showicon</key><true/><key>hotspot</key><string>0.78,0.65,0.05,0.05</string><key>autostart</key><false/><key>kind</key><string>jumpto</string><key>url</key><string>78</string></dict></array><key>number</key><integer>13</integer></dict></array></dict></plist>";
        XmlElement element1 = Xmlwise.createSimpleXml(plist);
        XmlElement element2 = Xmlwise.createXml(plist);
        assertEquals(element1, element2);
    }
	public void testCreateDocument() throws Exception
	{
		XmlElement e = Xmlwise.createXml("<x>foo</x>");
		assertEquals("<x>foo</x>", e.toXml());
	}

	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
	public void testFromFile() throws Exception
	{
		File f = File.createTempFile("xmltest", "xml");
		f.deleteOnExit();
		FileOutputStream out = new FileOutputStream(f);
		out.write("<xml><test/></xml>".getBytes());
		out.close();
		XmlElement e = Xmlwise.loadXml(f.getAbsolutePath());
		assertEquals("<xml><test/></xml>", e.toXml());
	}


	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
	public void testFromFileParseFail() throws Exception
	{
		File f = File.createTempFile("xmltest", "xml");
		f.deleteOnExit();
		FileOutputStream out = new FileOutputStream(f);
		out.write("<xml></mlx>".getBytes());
		out.close();
		try
		{
			Xmlwise.loadXml(f.getAbsolutePath());
			fail();
		}
		catch (XmlParseException e)
		{
		}
	}

	public void testFromFileIOException() throws Exception
	{
		try
		{
			Xmlwise.loadXml("not.a.file.that.exists");
			fail();
		}
		catch (IOException e)
		{
		}
	}

	public void testEscapeXML() throws Exception
	{
		assertEquals("&lt;&gt;&quot;&apos;&amp;", Xmlwise.escapeXML("<>\"'&"));
	}
}