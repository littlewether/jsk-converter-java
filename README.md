# JSK Converter

## 1. environment variable
This code was developed in OpenJDK8.  
Therefore, it is recommended to use JDK8 or higher version.

## 2. Using.
### 2-1. XML Conversion.
Support conversion list.
1. XML to Map.
2. XML to Beautify.
3. XML to Csv. (Developing...)
4. ~~XML to Json.~~ (Planning...)
5. ~~XML to Yaml.~~ (Planning...)

```java
String xml = "<person><name>js</name></person>";
XmlConvert xmlConvert = new XmlConvert();
Map<String, Object> map = xmlConvert.toMap(xml);
System.out.println(map.toString());
```

```java
String xml = "<person><name>js</name></person>";
XmlConvert xmlConvert = new XmlConvert();
String beautifyXml = xmlConvert.toBeautify(xml);
System.out.println(beautifyXml);
```