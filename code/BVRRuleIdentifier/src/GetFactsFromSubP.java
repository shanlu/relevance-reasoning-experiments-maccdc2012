package neu.proposal.facts;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import neu.proposal.rules.ReadKnowledgeBase;
import neu.proposal.rules.Triple;

public class GetFactsFromSubP {

	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	static final String inputFolderName  = "/home/shanlu/Documents/ont";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String relationURI = "";
	
	static final String subject = "";
	static final String object = "";
	static final String relation = "";
	static final String subject1 = "";
	static final String object1 = "";
	static final String object2 = "";
	
	public static boolean IsStringInArrayList(String goal, ArrayList<String> predicateList) {
		Iterator predicates = predicateList.iterator();
		
		while (predicates.hasNext()) {
			if (predicates.next().equals(goal)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean IsTripleInKnowledgeBase(Triple t, ArrayList<Triple> tripleList) {
		Iterator triples = tripleList.iterator();
		
		while (triples.hasNext()) {
			Triple triple = (Triple) triples.next();
			if (t.getSubject().equals(triple.getSubject()) && t.getPredicate().equals(triple.getPredicate()) && t.getObject().equals(triple.getObject())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		File file = new File(inputFileName);
		
		File folder = new File(inputFolderName);
		
		AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
		manager.addIRIMapper(mapper);
		
		SimpleIRIMapper iriMapper1 =  new SimpleIRIMapper(IRI.create("http://data-marking.mitre.org/DataMarking#"), IRI.create(new File("/home/shanlu/Documents/ont/DataMarking.owl")));
		manager.addIRIMapper(iriMapper1);
		SimpleIRIMapper iriMapper2 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/Common#"), IRI.create(new File("/home/shanlu/Documents/ont/CyboxCommon.owl")));
		manager.addIRIMapper(iriMapper2);
		SimpleIRIMapper iriMapper3 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/cybox_v1#"), IRI.create(new File("/home/shanlu/Documents/ont/Cybox.owl")));
		manager.addIRIMapper(iriMapper3);
		SimpleIRIMapper iriMapper4 =  new SimpleIRIMapper(IRI.create("http://maec.mitre.org/XMLSchema/maec-core-2#"), IRI.create(new File("/home/shanlu/Documents/ont/Maec.owl")));
		manager.addIRIMapper(iriMapper4);
		SimpleIRIMapper iriMapper5 =  new SimpleIRIMapper(IRI.create("http://capec.mitre.org/capec_v1#"), IRI.create(new File("/home/shanlu/Documents/ont/Capec.owl")));
		manager.addIRIMapper(iriMapper5);
		
		ReadKnowledgeBase readK = new ReadKnowledgeBase();
		ArrayList<Triple> tripleList = readK.ReadTripleFromOnt(inputFileName, base);
		
		ArrayList<Triple> relavantFacts = new ArrayList<Triple>();
		
		
		try {
			OWLOntology stix = manager.loadOntologyFromOntologyDocument(file);
			
			IRI documentIRI = manager.getOntologyDocumentIRI(stix);
			
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			OWLObjectProperty scans = dataFactory.getOWLObjectProperty(
					IRI.create(base + "#scans"));
			OWLObjectProperty connects = dataFactory.getOWLObjectProperty(
					IRI.create(base + "#connects"));
			
			
			if (!stix.getObjectSubPropertyAxiomsForSubProperty(connects).isEmpty()) {
				String subAxiom = stix.getObjectSubPropertyAxiomsForSubProperty(connects).toString();
				System.out.println(subAxiom);
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("\\<(.*?)\\>");
				Matcher regexMatcher = regex.matcher(subAxiom);

				while (regexMatcher.find()) {//Finds Matching Pattern in String
				   matchList.add(regexMatcher.group(1));//Fetching Group from String
				}
				System.out.println(matchList.get(1));
				
				String superRelation = matchList.get(1);
				
				Triple tSuper = new Triple();
				tSuper.setSubject(subject);
				tSuper.setPredicate(superRelation);
				tSuper.setObject(object);
				
				if (IsTripleInKnowledgeBase(tSuper, tripleList)) {
					relavantFacts.add(tSuper);
				}
				
			}
			
			System.out.println(stix.getTransitiveObjectPropertyAxioms(connects));
			
			if (!stix.getTransitiveObjectPropertyAxioms(connects).isEmpty()) {
				Triple t1 = new Triple();
				t1.setSubject(subject1);
				t1.setPredicate(relation);
				t1.setObject(object1);
				Triple t2 = new Triple();
				t2.setSubject(object1);
				t2.setPredicate(relation);
				t2.setObject(object2);
				if (IsTripleInKnowledgeBase(t1, tripleList) && IsTripleInKnowledgeBase(t2, tripleList)) {
					
				}
			}
			
			System.out.println(stix.getEquivalentObjectPropertiesAxioms(connects));
			
			if (!stix.getEquivalentObjectPropertiesAxioms(connects).isEmpty()) {
				String eqAxiom = stix.getEquivalentObjectPropertiesAxioms(connects).toString();
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("\\<(.*?)\\>");
				Matcher regexMatcher = regex.matcher(eqAxiom);

				while (regexMatcher.find()) {//Finds Matching Pattern in String
				   matchList.add(regexMatcher.group(1));//Fetching Group from String
				}
				System.out.println(matchList.get(1));
				
				String eqRelation = matchList.get(1);
				
				Triple tEq = new Triple();
				tEq.setSubject(subject);
				tEq.setPredicate(eqRelation);
				tEq.setObject(object);
				
				if (IsTripleInKnowledgeBase(tEq, tripleList)) {
					relavantFacts.add(tEq);
				}
				
			}
			
			System.out.println(stix.getInverseObjectPropertyAxioms(scans));
			
			if (!stix.getInverseObjectPropertyAxioms(scans).isEmpty()) {
				String invAxiom = stix.getInverseObjectPropertyAxioms(scans).toString();
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("\\<(.*?)\\>");
				Matcher regexMatcher = regex.matcher(invAxiom);

				while (regexMatcher.find()) {//Finds Matching Pattern in String
				   matchList.add(regexMatcher.group(1));//Fetching Group from String
				}
				System.out.println(matchList.get(0));
				
				String invRelation = matchList.get(0);
				
				Triple tInv = new Triple();
				tInv.setSubject(object);
				tInv.setPredicate(invRelation);
				tInv.setObject(subject);
				
				if (IsTripleInKnowledgeBase(tInv, tripleList)) {
					relavantFacts.add(tInv);
				}
				
			}
			
			
		} catch (OWLOntologyInputSourceException | OWLOntologyCreationException ex) {
            // throw custom exception
        }
		
		
		
	}
	
	
	
	
}
