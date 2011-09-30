package xmlwise;
/**
 * @author Christoffer Lerno 
 */

import junit.framework.*;
import xmlwise.Plist;

import java.util.*;
import java.io.File;
import java.io.Closeable;
import java.io.IOException;

public class PlistTest extends TestCase
{
	Plist m_plist = new Plist();


	public void testParse() throws Exception
	{
		Map<String, Object> result = quickParse(
				"<key>1</key><array>" +
				"<integer>1</integer>" +
				"<real>1.0</real>" +
				"<string>test&amp;</string>" +
				"<date>1998-01-02T11:22:33Z</date>" +
				"<true/>" +
				"<false/></array>");
		assertEquals("{1=[1, 1.0, test&, Fri Jan 02 12:22:33 CET 1998, true, false]}", result.toString());
	}

	public void testToXml() throws Exception
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", 3);
		map.put("b", 1.0);
		map.put("c", "foo");
		ArrayList list = new ArrayList();
		list.add("a");
		list.add(3);
		map.put("f", list);
		assertEquals(map, Plist.fromXml(Plist.toXml(map)));
		File temp = File.createTempFile("plist", "foo");
		temp.deleteOnExit();
		Plist.store(map, temp.getAbsolutePath());
		assertEquals(map, Plist.load(temp.getAbsolutePath()));
	}

	public void testLongVsInt() throws Exception
	{
		assertEquals(Long.class, quickParse("<key>a</key><integer>" + Long.MAX_VALUE + "</integer>").get("a").getClass());
		assertEquals(Integer.class, quickParse("<key>a</key><integer>" + Integer.MAX_VALUE + "</integer>").get("a").getClass());
		assertEquals(Long.class, quickParse("<key>a</key><integer>" + (Integer.MAX_VALUE + 1L) + "</integer>").get("a").getClass());
	}

	private Map<String, Object> quickParse(String string) throws XmlParseException
	{
		return m_plist.parse(Xmlwise.createXml("<plist><dict>" + string + "</dict></plist>"));
	}

    public void testParseProblematicPlist() throws Exception
    {
        Plist.fromXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\"><plist version=\"1.0\"><dict><key>general</key><dict><key>cbid</key><string>451631</string><key>hrid</key><string>sp2624</string><key>firstName</key><string>SUDHARANI</string><key>lastName</key><string>PARVANGADA</string><key>primaryPhone</key><string>+1 7324208039</string><key>primaryEmail</key><string>sparvang@us.ibm.com</string><key>pagerEmailMaxlen</key><string>110</string><key>pagerEmailMaxmsgs</key><string>5</string><key>celEmailMaxlen</key><string>110</string><key>celEmailMaxmsgs</key><string>5</string><key>lastModifier</key><string>451471</string><key>dateModified</key><string>23-Sep-2009 04:58:20 PM</string><key>dateModifiedLong</key><string>1253739500000</string><key>timeZone</key><string>US/Eastern</string><key>timeZoneId</key><string>13</string><key>permission</key><string>15</string><key>privilege</key><string>AOTS_User_Group</string><key>source</key><string>C-Bus User</string></dict><key>pmoc</key><array><dict><key>startHour</key><string>0</string><key>endHour</key><string>24</string><key>startMinute</key><string>0</string><key>endMinute</key><string>0</string><key>dayBit</key><string>126</string><key>active</key><string>Yes</string><key>contactMode</key><string>1</string><key>contactModeString</key><string>Primary_Email</string><key>source</key><string>Profile</string><key>field</key><string>Primary Email</string><key>workTime</key><string>0:0-24:0</string><key>advancedRetry</key><string>5</string><key>advancedRetryTimes</key><string>1</string><key>pmc_id</key><string>1297706</string></dict></array><key>roles</key><array><dict><key>role</key><string>RequesterId</string><key>roleId</key><string>7</string><key>roleValue</key><string>sp2624</string><key>active</key><string>Y</string><key>activeBits</key><string>1</string><key>isImplicit</key><string>N</string></dict></array><key>implicitRoles</key><array><dict><key>role</key><string>LastModifiedBy</string><key>roleId</key><string>23</string><key>roleValue</key><string>sp2624</string><key>active</key><string>Y</string><key>activeBits</key><string>2</string><key>isImplicit</key><string>Y</string></dict></array></dict></plist>");
        Plist.fromXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                      "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                      "<plist version=\"1.0\"><dict><key>pages</key><array><dict><key>uid</key><string>page1</string><key>widgets</key><array><dict><key>fullscreen</key><false/><key>content</key><string>0.13,0.27,0.72,0.34</string><key>uid</key><string>page1w01</string><key>showoverlay</key><false/><key>popup</key><false/><key>showicon</key><true/><key>hotspot</key><string>0.26,0.76,0.58,0.2</string><key>autostart</key><false/><key>kind</key><string>video</string><key>url</key><string>backstage_conv.mp4</string></dict></array><key>number</key><integer>1</integer></dict><dict><key>uid</key><string>page7</string><key>widgets</key><array><dict><key>fullscreen</key><true/><key>content</key><string>0.0,0.0,0.5,0.5</string><key>uid</key><string>page7w01</string><key>showoverlay</key><true/><key>popup</key><false/><key>showicon</key><false/><key>hotspot</key><string>0.21,0.45,0.57,0.08</string><key>autostart</key><false/><key>kind</key><string>link</string><key>url</key><string>http://www.elle.it</string></dict></array><key>number</key><integer>7</integer></dict><dict><key>uid</key><string>page8</string><key>widgets</key><array><dict><key>fullscreen</key><false/><key>content</key><string>0.05,0.07,0.91,0.55</string><key>uid</key><string>page8w01</string><key>showoverlay</key><true/><key>popup</key><true/><key>showicon</key><false/><key>hotspot</key><string>0.04,0.86,0.09,0.07</string><key>autostart</key><false/><key>kind</key><string>link</string><key>url</key><string>http://www.hachettepubblicita.it</string></dict></array><key>number</key><integer>8</integer></dict><dict><key>uid</key><string>page11</string><key>widgets</key><array><dict><key>fullscreen</key><false/><key>content</key><string>0.0,0.0,0.5,0.5</string><key>uid</key><string>page11w01</string><key>showoverlay</key><false/><key>popup</key><false/><key>showicon</key><true/><key>hotspot</key><string>0.44,0.27,0.48,0.36</string><key>autostart</key><false/><key>kind</key><string>audio</string><key>url</key><string>06-Eh...già.mp3</string></dict></array><key>number</key><integer>11</integer></dict><dict><key>uid</key><string>page13</string><key>widgets</key><array><dict><key>fullscreen</key><false/><key>content</key><string>0.19,0.03,0.64,0.64</string><key>uid</key><string>page13w01</string><key>showoverlay</key><false/><key>popup</key><true/><key>showicon</key><true/><key>hotspot</key><string>0.51,0.87,0.16,0.05</string><key>autostart</key><false/><key>kind</key><string>pdf</string><key>url</key><string>PROVA_MM_01m01.pdf</string></dict><dict><key>fullscreen</key><false/><key>content</key><string>0.0,0.0,0.5,0.5</string><key>uid</key><string>page13w11</string><key>showoverlay</key><false/><key>popup</key><true/><key>showicon</key><true/><key>hotspot</key><string>0.78,0.65,0.05,0.05</string><key>autostart</key><false/><key>kind</key><string>jumpto</string><key>url</key><string>78</string></dict></array><key>number</key><integer>13</integer></dict></array></dict></plist>");
    }

	public void testParseNonPlist()
	{
		try
		{
			Plist.fromXml("<dict></dict>");
		}
		catch (XmlParseException e)
		{
			assertEquals("Expected plist top element, was: dict", e.getMessage());
		}
	}

	public void testParseBytes() throws Exception
	{
		byte[] bytes = new byte[]{1, 2, (byte) 255};
		String bytesBase64 = Plist.base64encode(bytes);

		Map<String, Object> result = quickParse("<key>a</key><data>" + bytesBase64 + "</data>");
		assertEquals("[1, 2, -1]", Arrays.toString((byte[]) result.get("a")));
	}

	public void testFailUnknownType()
	{
		try
		{
			quickParse("<key>a</key><foo>bar</foo>");
			fail();
		}
		catch (XmlParseException e)
		{
			assertEquals("Failed to parse: <dict><key>a</key><foo>bar</foo></dict>", e.getMessage());
		}
	}

	public void testFailUnexpectedKey()
	{
		try
		{
			quickParse("<string>bar</string>");
			fail();
		}
		catch (XmlParseException e)
		{
			assertEquals("Failed to parse: <dict><string>bar</string></dict>", e.getMessage());
		}
	}

	public void testFailToParseBytes()
	{
		try
		{
			quickParse("<key>a</key><data>!</data>");
			fail();
		}
		catch (XmlParseException e)
		{
			assertEquals("Failed to parse: <dict><key>a</key><data>!</data></dict>", e.getMessage());
		}
	}

	private String toXml(Object o)
	{
		return m_plist.objectToXml(o).toXml();
	}

	public void testObjectToXml() throws Exception
	{
		assertEquals("<string>&lt;&amp;&gt;</string>", toXml("<&>"));
		assertEquals("<integer>-1</integer>", toXml(-1));
		assertEquals("<real>0.1</real>", toXml(0.1));
		assertEquals("<date>1970-01-01T00:00:00Z</date>", toXml(new Date(0)));
		assertEquals("<true/>", toXml(true));
		assertEquals("<false/>", toXml(false));
		assertEquals("<array><array><string>Foo</string><integer>0</integer></array></array>",
		             toXml(Arrays.asList(Arrays.asList("Foo", 0))));
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put("b", 0.1);
		map.put("a", "foo");
		assertEquals("<dict><key>a</key><string>foo</string><key>b</key><real>0.1</real></dict>", toXml(map));
		assertEquals("<data>Rm9vYmFy</data>", toXml("Foobar".getBytes()));
		toXml((byte) 1);
		toXml((float) 1);
		toXml((short) 1);
		toXml((long) 1);
		try
		{
			toXml(new Object());
			fail();
		}
		catch (Exception e)
		{
			assertEquals("Cannot use class java.lang.Object in plist.", e.getMessage());
		}
	}

	public void testDecode() throws Exception
	{
		assertEquals("Foobar", new String(Plist.base64decode(" " + Plist.base64encode("Foobar".getBytes()))));
		assertEquals("Foobar!", new String(Plist.base64decode(Plist.base64encode("Foobar!".getBytes()))));
		assertEquals("Foobar!!", new String(Plist.base64decode(Plist.base64encode("Foobar!!".getBytes()))));
        assertEquals("Foobar!!", new String(Plist.base64decode("Rm9v\n YmF yISE=")));
	}

    public void testObjectDecodeAndEncode() throws Exception
    {
        assertEquals("A String!", Plist.objectFromXml(Plist.toPlist("A String!")));
        File temp = File.createTempFile("plist", "foo");
        temp.deleteOnExit();
        Plist.storeObject(123, temp.getAbsolutePath());
        assertEquals(123, Plist.loadObject(temp.getAbsolutePath()));
    }
    
	public void testEncode() throws Exception
	{
		assertEquals("Rm9vYmFy", Plist.base64encode("Foobar".getBytes()));
		assertEquals("Rm9vYmFyIQ==", Plist.base64encode("Foobar!".getBytes()));
		assertEquals("Rm9vYmFyISE=", Plist.base64encode("Foobar!!".getBytes()));
	}

	public void testSilentlyClose() throws Exception
	{
		Plist.silentlyClose(new Closeable()
		{
			public void close() throws IOException
			{
				throw new IOException();
			}
		});
		Plist.silentlyClose(null);
	}
}