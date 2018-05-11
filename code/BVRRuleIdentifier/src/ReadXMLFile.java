package neu.proposal.rules;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;

public class ReadXMLFile {

  public static void main(String argv[]) {

    try {

	File fXmlFile = new File("/home/shanlu/Documents/scans_rule.bvr");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);

	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();
	

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

	NodeList ruleList = doc.getElementsByTagName("rule");

	System.out.println("----------------------------");

	for (int temp = 0; temp < ruleList.getLength(); temp++) {

		Node ruleNode = ruleList.item(temp);
		System.out.println("\nCurrent Element :" + ruleNode.getNodeName());

		if (ruleNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) ruleNode;
			System.out.println("rule name: " + eElement.getAttribute("name")); 
			Rule rule = new Rule();
			rule.setName(eElement.getAttribute("name"));
			
			System.out.println("---------body-----------");
			Node bodyNode = doc.getElementsByTagName("body").item(temp);
			Element bodyElement = (Element) bodyNode;
			NodeList bodyTripleList = bodyElement.getElementsByTagName("triple");
			ArrayList<Triple> body = new ArrayList<Triple>();
			
			for (int i = 0; i < bodyTripleList.getLength(); i++) {
				Node bodyTripleNode = bodyTripleList.item(i);
				Element tripleElement = (Element) bodyTripleNode;
				
				Triple triple = new Triple();
				Node predicateNode = tripleElement.getElementsByTagName("predicate").item(0);
				Element predicateElement = (Element) predicateNode;
				if (predicateElement.getAttribute("resource") == "") {
					System.out.println("variable : " + predicateElement.getAttribute("variable"));
					triple.setPredicate("variable:" + predicateElement.getAttribute("variable"));
				}
				else {
					System.out.println("resource : " + predicateElement.getAttribute("resource"));
					triple.setPredicate(predicateElement.getAttribute("resource"));
				}
				body.add(triple);
			}
			
			System.out.println("----------head----------");
			
			Node headNode = doc.getElementsByTagName("head").item(temp);
			Element headElement = (Element) headNode;
			NodeList headTripleList = headElement.getElementsByTagName("triple");
			ArrayList<Triple> head = new ArrayList<Triple>();
			
			for (int i = 0; i < headTripleList.getLength(); i++) {
				Node headTripleNode = headTripleList.item(i);
				Element tripleElement = (Element) headTripleNode;
				
				Triple triple = new Triple();
				Node predicateNode = tripleElement.getElementsByTagName("predicate").item(0);
				Element predicateElement = (Element) predicateNode;
				if (predicateElement.getAttribute("resource") == "") {
					System.out.println("variable : " + predicateElement.getAttribute("variable"));
					triple.setPredicate("variable:" + predicateElement.getAttribute("variable"));
				}
				else {
					System.out.println("resource : " + predicateElement.getAttribute("resource"));
					triple.setPredicate(predicateElement.getAttribute("resource"));
				}
				head.add(triple);
			}

		}
	}
    } catch (Exception e) {
	e.printStackTrace();
    }
    
    
  }

}