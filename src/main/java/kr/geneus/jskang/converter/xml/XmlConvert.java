package kr.geneus.jskang.converter.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import kr.geneus.jskang.converter.common.Convert;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Slf4j
public class XmlConvert implements Convert {

    private DocumentBuilderFactory factory = null;
    private DocumentBuilder builder = null;
    private String xmlCode = "";

    public XmlConvert() throws ParserConfigurationException {
        this.factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
    }

    public XmlConvert(String xmlCode) throws ParserConfigurationException {
        this.factory = DocumentBuilderFactory.newInstance();
        this.builder = factory.newDocumentBuilder();
        this.xmlCode = xmlCode;
    }

    public Boolean setSource(String xmlCode) {
        this.xmlCode = xmlCode;
        return true;
    }

    @Override
    public Map<String, Object> buildToMap() throws IOException {
        if (this.xmlCode.isEmpty()) {
            throw new NullPointerException("The value to convert is empty.");
        }

        Map<String, Object> json = new LinkedHashMap<String, Object>();
        Document document = null;
        try {
            StringReader stringReader = new StringReader(this.xmlCode);
            InputSource inputSource = new InputSource(stringReader);
            document = this.builder.parse(inputSource);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        }

        String rootName = document.getFirstChild().getNodeName();
        Map<String, Object> map = getNodeList(document, rootName);
        log.debug("[XML to MAP Result]");
        log.debug(map.toString());
        return map;
    }

    private Map<String, Object> getNodeList(Document doc, String rootName) {
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName(rootName);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(rootName, null);
        log.debug("XML RootName: " + rootName);

        return travNode(map.get(rootName), nList);
    }

    private Map travNode(Object map, NodeList nodes) {

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);

            if (n.getNodeType() == Node.ELEMENT_NODE) {
                NodeList childNodes = n.getChildNodes();
                int cnt = childNodes.getLength();

                if (cnt == 1) {
                    if (map == null) {
                        map = new LinkedHashMap<>();
                        log.debug("XML create linkedHashMap.");
                    }
                    ((Map) map).put(n.getNodeName(), n.getTextContent());
                } else {
                    if (map == null) {
                        map = new LinkedHashMap<>();
                        log.debug("XML create linkedHashMap.");
                    }
                    ((Map) map).put(n.getNodeName(), null);
                }

                if (cnt > 1) {
                    ((Map) map).put(n.getNodeName(), travNode(((Map) map).get(n.getNodeName()), childNodes));
                }
            }
        }
        return (Map) map;
    }
}