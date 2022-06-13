package sample.xpath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * XML Node를 검색하는 정규식의 일종인 XPath를 이용하여 Node를 검색하는 예제
 *
 * XPath syntax
 * [node명] : 노드명에 해당하는 모든 노드
 * [/]        : root node검색
 * [//]        : 위치에 상관 없이 해당하는 node를 검색
 * [.]        : 현재 위치에 있는 노드
 * [..]        : 현재 위치에서 상위에 있는 노드
 * [@~]        : 속성으로 노드를 검색
 *
 * 예제
 * <최상위 노드를 검색하는게 아니라 중간에 있는 모든 노드를 검색할때에는 앞에 //를 붙인다>
 * XML에서 step1이란 node를 검색 : step1
 *
 * step1내의 step2 노드 검색 : step1/step2
 *
 * XML에서 name속성을 가지는 노드 검색 : //@name
 *
 * 노드 리스트중 특정위치의 값 검색 : //step1/step2[1]
 * - 주의점 IE5,6,7,8,9에서는 첫번째 노드가 [0]이지만 W3C표준에서는 첫번째 노드가[1]임 이를 해결하기 위해서는 XPath에 SelectionLanguage를 설정(web상 javascript에서 실행하지 않는이상은 신경쓸 필요가 없음)
 *
 * 노드 리스트중 마지막위치 값 검색 : //step1/step2[last()]
 *
 * 노드 리스트중 특정 index전 값 검색 : //step1/step2[position() < 3]
 *
 * 특정 속성명을 가진 노드 검색 : //step1[@name='특정값']
 *
 * 특정 속성명을 가진 모든노드 검색 : //*[@name='특정값']
 *
 * 특정조건을 만족하는 노드 검색(step2 노드 안의 price 노드값이 35이상인 데이터 추출) : //step1/step2[price > 35]
 * //step1/step2[price > 35]/step3 이런식으로도 사용가능
 *
 * 노드값이 특정문자열을 포함하는 노드만 검색 : //*[contains(@path,'RDR')] (path에 RDR이란 값이 포함되는 노드만 검색)
 *
 * 선택한 노드의 형제노드 검색
 * /AAA/BBB/following-sibling::*
 *
 * 노드안의 텍스트를 가지고 검색
 * /AAA/BBB[contains(text(),'ZZZ')]
 *
 * 특정 Node명만 검색 <Name>,<Name-aaa>,<Name-222>
 * //*[contains(local-name(), 'Name')]
 *
 * 이미 검색된 Node아래에서 검색하고자 할때
 *  NodeList prmtList = (NodeList)xpath.evaluate("//ReleaseDeal/DealReleaseReference[text() = 'R0']/following-sibling::Deal/DealTerms", doc, XPathConstants.NODESET);
    ArrayList<String> tempList = new ArrayList<String>();

    for(int i = 0 ; i<prmtList.getLength() ; i++) {
        Node test = (Node)xpath.evaluate(".//CommercialModelType", prmtList.item(i), XPathConstants.NODE);
        System.out.println("test : "+test.getTextContent());
    }

    //CommercialModelType - document root 아래의 모든 CommercialModelType 노드를 검색
    .//CommercialModelType - 현재 검색된 Node아래 에서 모든 CommercialModelType 노드를 검색
 *
 * 아래 예제에서 /AAA/BBB/following-sibling::*을 실행할경우 BBB와 동일한 레벨에 있는 노드인 XXX,CCC노드가 검색된다.
 * ex)
 *    <AAA>
        <BBB>
            <CCC/>
            <DDD/>
        </BBB>
        <XXX>
            <DDD>
                <EEE/>
            <DDD/>
            <CCC/>
            <FFF/>
            <FFF>
                <GGG/>
            </FFF>
            </DDD>
        </XXX>
        <CCC>
            <DDD/>
        </CCC>
    </AAA>
 */
public class XPathSample {
    /*
     * 각 노드별로 최상위 ~ 상위까지의 문자열을 반환한다.
     */
    public String getPathName(Node node) {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();

        String parentNodeName = "";
        Node parentNode;
        while(!"root".equals(parentNodeName)) {
            parentNode = node.getParentNode();
            parentNodeName = parentNode.getNodeName();

            list.add(parentNode.getAttributes().getNamedItem("name").getNodeValue());
            node = parentNode;
        }

        Collections.reverse(list);

        list.forEach(row -> sb.append(row).append(" > "));

        return sb.toString();
    }

    //Document 객체 반환
    public Document getDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // to be compliant, completely disable DOCTYPE declaration:
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        // or completely disable external entities declarations:
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        // or prohibit the use of all protocols by external entities:
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse("d:/data_tree.xml");
    }

    //XML수정내용을 XML파일에 반영
    public void applyXML(Document doc) throws TransformerException {
        TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
        // to be compliant, prohibit the use of all protocols by external entities:
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult("d:/data_tree.xml");
        transformer.transform(source, result);
    }

    //Node List 를 arrayList 로 변환
    public List<HashMap<String,String>> getXmlList(NodeList nodeList) {

        List<HashMap<String,String>> list = new ArrayList<>();
        for(int i = 0 ; i<nodeList.getLength() ; i++) {
            HashMap<String,String> map = new HashMap<>();
            Node node = nodeList.item(i);
            NamedNodeMap nodeMap = node.getAttributes();

            if(nodeMap.getNamedItem("path") == null) {
                continue;
            }

            map.put("name", nodeMap.getNamedItem("name").getNodeValue());
            map.put("type", nodeMap.getNamedItem("type").getNodeValue());
            map.put("path", nodeMap.getNamedItem("path").getNodeValue());
            map.put("rootPath", getPathName(node));

            list.add(map);
        }

        return list;
    }

    //Node에 속성 추가
    public void addAttribute(Node node) {
        Element e = (Element)node;
        e.setAttribute("test", "test");
    }

    //Node 추가
    public void addNode(Document doc,Node node) {
        Element age = doc.createElement("child");
        node.getParentNode().appendChild(age);
    }

    private void getValueTest() {
        try {
            Document doc = new XPathSample().getDocument();
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

            NodeList nodeList = (NodeList)xpath.evaluate("//*[@name='실황감시']/*[@name='합성']/*[@name='실시간(일기현상)']", doc, XPathConstants.NODESET);

            System.out.println("find node list length : "+nodeList.getLength());

            for(int i = 0 ; i<nodeList.getLength() ; i++) {
                Node node = nodeList.item(i);
                System.out.println("find node : "+node);
                System.out.println("find node type attr value : "+node.getAttributes().getNamedItem("type").getNodeValue());
            }
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    private void modifyXmlTest() {
        try {
            Document doc = new XPathSample().getDocument();
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

            NodeList nodeList = (NodeList)xpath.evaluate("//*[@name='실황감시']/*[@name='합성']/*[@name='실시간(일기현상)']", doc, XPathConstants.NODESET);

            // node modify
            Node node = nodeList.item(0);
            Element e = (Element)node;
            e.setAttribute("test", "test");

            // node add
            Element age = doc.createElement("child");
            node.getParentNode().appendChild(age);

            applyXML(doc);
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        XPathSample xPathSample = new XPathSample();
        xPathSample.getValueTest();
        xPathSample.modifyXmlTest();
    }
}
