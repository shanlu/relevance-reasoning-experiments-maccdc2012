package neu.proposal.axiom;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GetPropertyAxiom {
	static final String inputFileName  = "/home/shanlu/Documents/example.owl";
	static final String objectProperty = "http://www.semanticweb.org/shanlu/ontologies/2017/5/untitled-ontology-87#isFriendOf";
	
	public static ArrayList<String> GetPropertyAxioms (String property, String inputFileName) {
		ArrayList<String> axioms = new ArrayList<String>();
		
		try {
			File fXmlFile = new File(inputFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("owl:ObjectProperty");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println(eElement.getAttribute("rdf:about"));
					if (eElement.getAttribute("rdf:about").equals(objectProperty)) {
					NodeList axiomLists = eElement.getElementsByTagName("rdf:type");
					if (axiomLists != null) {
						for (int i = 0; i < axiomLists.getLength(); i++) {
							Node axiom = axiomLists.item(i);
							Element eAxiom = (Element) axiom;
							System.out.println(eAxiom.getAttribute("rdf:resource"));
						}
					}
					}
				}
			}

			} catch (Exception e) {
				e.printStackTrace();
		    }
		
		return axioms;
	}
	
//	public static void main(String argv[]) {
//	
//	try {
//		File fXmlFile = new File(inputFileName);
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//		Document doc = dBuilder.parse(fXmlFile);
//		
//		doc.getDocumentElement().normalize();
//		
//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//		
//		NodeList nList = doc.getElementsByTagName("owl:ObjectProperty");
//		for (int temp = 0; temp < nList.getLength(); temp++) {
//			Node nNode = nList.item(temp);
//			System.out.println("\nCurrent Element :" + nNode.getNodeName());
//			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//				Element eElement = (Element) nNode;
//				System.out.println(eElement.getAttribute("rdf:about"));
//				if (eElement.getAttribute("rdf:about").equals(objectProperty)) {
//				NodeList axiomLists = eElement.getElementsByTagName("rdf:type");
//				if (axiomLists != null) {
//				for (int i = 0; i < axiomLists.getLength(); i++) {
//					Node axiom = axiomLists.item(i);
//					Element eAxiom = (Element) axiom;
//					System.out.println(eAxiom.getAttribute("rdf:resource"));
//				}
//				}
//				}
//			}
//		}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//	    }
//	
//	}

}
