Xmlwise aims to make reading and writing of simple xml-files painless.

Xmlwise takes a parsed XML document and, assuming a simple xml-file format, extracts text nodes and attributes.

Here is how Xmlwise can help you access your XML structure:

The XML:

```
<records>
  <cd>
    <title>Edward Scissorhands</title>
    <grouping type="genre">Soundtrack</grouping>
    <grouping type="user-defined">Tim Burton Movies</grouping>
  </cd>
  <cd>
    <title>Secession</title>
    <grouping type="genre">Goth</grouping>
  </cd>
</records>
```

With Xmlwise:

```
// Loading an xml file
XmlElement recordsNode = Xmlwise.loadXml("records.xml");

// Retrieving a sub element
XmlElement firstCD = recordsNode.get(0); // The first cd element.

// Retriving a single unique sub element
XmlElement title = firstCD.getUnique("title");

// Getting the text value of an element
title.getValue(); // => "Edward Scissorhands"

// Selecting a group of elements based on name
List<XmlElement> groupings = title.get("grouping");

// Reading an attribute
groupings.get(0).getAttribute("type"); // => "genre"
```

Xmlwise also offers painless handling of Apple plist files:

```
Map<String, Object> properties = Plist.load("myproperties.plist"); // loads the (nested) properties.
```