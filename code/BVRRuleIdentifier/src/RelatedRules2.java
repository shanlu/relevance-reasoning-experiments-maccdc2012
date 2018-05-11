package neu.proposal.rules;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

public class RelatedRules2 {
	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	static final String baseURI = "http://stix.mitre.org/STIX";
	
	static final String STOL  = "/home/shanlu/Documents/ont/STO-L.owl";
	static final String stoURI = "http://www.vistology.com/ont/2013/STO-L.owl";
	
	static final String fileName = "/home/shanlu/Documents/scans_rule_3.bvr";
	static final String Goal = "http://stix.mitre.org/STIX#scans";
	
	public static boolean IsStringInArrayList(String goal, ArrayList<String> predicateList) {
		Iterator predicates = predicateList.iterator();
		while (predicates.hasNext()) {
			if (predicates.next().equals(goal)) {
				return true;
			}
		}
		return false;
	}
	
public static void main(String[] args) {
	    ReadRules readR = new ReadRules();
		ReadKnowledgeBase readK = new ReadKnowledgeBase();
		
		ArrayList<Rule> ruleList = readR.ReadRuleDateset(fileName);
		ArrayList<String> subjectList = readK.ReadSubjectFromOnt(inputFileName, baseURI);
	    
		ArrayList<Rule> relatedRules = new ArrayList<Rule>();
		
		Stack<String> goals = new Stack<String>();
		goals.push(Goal);
		
		while (!goals.isEmpty()) {
			String goal = goals.pop();
			if (IsStringInArrayList(goal, subjectList)) {
				goal = goals.pop();
			}
			else {
			
			Iterator<Rule> rules = ruleList.iterator();
			while (rules.hasNext()) {
				Rule rule = rules.next();
				ArrayList<Triple> head = rule.head;
				Iterator<Triple> headTris = head.iterator();
				
				while (headTris.hasNext()) {
					String p = headTris.next().subject;
					if (p.equals(goal)) {
						relatedRules.add(rule);
						System.out.println("Relevant rule is: " + rule.name);
						ArrayList<Triple> body = rule.body;
						Iterator<Triple> bodyTris = body.iterator();
						while (bodyTris.hasNext()) {
							Triple bodyTri = bodyTris.next();
							String bodyPredicate = bodyTri.subject;
							
							if (!IsStringInArrayList(bodyPredicate, subjectList)) {
								goals.push(bodyPredicate);
							}
						}
						break;
					}
				}
			}
		}
		}
}

}
