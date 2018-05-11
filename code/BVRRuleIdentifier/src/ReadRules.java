package neu.proposal.rules;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadRules {
	
	public ArrayList<Rule> ReadRuleDateset(String FileName) {
		ArrayList<Rule> ruleDataset = new ArrayList<Rule>();
		
		try {
		File fXmlFile = new File(FileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList ruleList = doc.getElementsByTagName("rule");

		for (int temp = 0; temp < ruleList.getLength(); temp++) {

			Node ruleNode = ruleList.item(temp);
			if (ruleNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) ruleNode;
//				System.out.println("rule name: " + eElement.getAttribute("name")); 
				Rule rule = new Rule();
				rule.setName(eElement.getAttribute("name"));
				
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
						triple.setPredicate("variable:" + predicateElement.getAttribute("variable"));
					}
					else {
						triple.setPredicate(predicateElement.getAttribute("resource"));
					}
					
					Node subjectNode = tripleElement.getElementsByTagName("subject").item(0);
					Element subjectElement = (Element) subjectNode;
					if (subjectElement.getAttribute("resource") == "") {
						triple.setSubject("variable:" + subjectElement.getAttribute("variable"));
					}
					else {
						triple.setSubject(subjectElement.getAttribute("resource"));
					}
					
					Node objectNode = tripleElement.getElementsByTagName("object").item(0);
					Element objectElement = (Element) objectNode;
					if (objectElement.getAttribute("resource") == "") {
						triple.setObject("variable:" + objectElement.getAttribute("variable"));
					}
					else {
						triple.setObject(objectElement.getAttribute("resource"));
					}
					
					body.add(triple);
				}
				rule.setBody(body);
				
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
						triple.setPredicate("variable:" + predicateElement.getAttribute("variable"));
					}
					else {
						triple.setPredicate(predicateElement.getAttribute("resource"));
					}
					
					Node subjectNode = tripleElement.getElementsByTagName("subject").item(0);
					Element subjectElement = (Element) subjectNode;
					if (subjectElement.getAttribute("resource") == "") {
						triple.setSubject("variable:" + subjectElement.getAttribute("variable"));
					}
					else {
						triple.setSubject(subjectElement.getAttribute("resource"));
					}
					
					Node objectNode = tripleElement.getElementsByTagName("object").item(0);
					Element objectElement = (Element) objectNode;
					if (objectElement.getAttribute("resource") == "") {
						triple.setObject("variable:" + objectElement.getAttribute("variable"));
					}
					else {
						triple.setObject(objectElement.getAttribute("resource"));
					}
					
					head.add(triple);
				}
				rule.setHead(head);
				
				ruleDataset.add(rule);
			}
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		    }
		
		return ruleDataset;
	}
	
	

}
