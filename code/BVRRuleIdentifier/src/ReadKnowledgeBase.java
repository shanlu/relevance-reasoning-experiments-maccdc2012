package neu.proposal.rules;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

public class ReadKnowledgeBase {
	
	
	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	static final String baseURI = "http://stix.mitre.org/STIX";
	
	static final String STOL  = "/home/shanlu/Documents/ont/STO-L.owl";
	static final String stoURI = "http://www.vistology.com/ont/2013/STO-L.owl";
	
	public static boolean IsStringInArrayList(String goal, ArrayList<String> predicateList) {
		Iterator predicates = predicateList.iterator();
		
		while (predicates.hasNext()) {
			if (predicates.next().equals(goal)) {
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<String> ReadPredicateFromOnt(String inputFileName, String baseURI) {
		ArrayList<String> predicateList = new ArrayList<String>();
		
		OntModel m = ModelFactory.createOntologyModel();
	    InputStream in = FileManager.get().open(inputFileName);
	    
	    if (in == null) {
	        throw new IllegalArgumentException("File: " + inputFileName + " not found");
	    }
	    m.read(in, null);
	    
	    
	    StmtIterator statementList = m.listStatements();
	    while (statementList.hasNext()) {
	    	Property p = statementList.next().getPredicate();
	    	String predicate = p.getURI();
//	    	predicate = predicate.substring(predicate.lastIndexOf("#")+1);
	    	if (predicate.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
	    		predicate = "rdf:type";
	    	}
	    	predicateList.add(predicate);
//    		System.out.println(predicate);
	    }
	    
	    return predicateList;
	}
	
	public ArrayList<String> ReadSubjectFromOnt(String inputFileName, String baseURI) {
		ArrayList<String> subjectList = new ArrayList<String>();
		
		OntModel m = ModelFactory.createOntologyModel();
	    InputStream in = FileManager.get().open(inputFileName);
	    
	    if (in == null) {
	        throw new IllegalArgumentException("File: " + inputFileName + " not found");
	    }
	    m.read(in, null);
	    
	    in = FileManager.get().open(STOL);
	    if (in == null) {
	        throw new IllegalArgumentException("File: " + STOL + " not found");
	    }
	    m.read(in, null);
	    
	    OntClass Relation = m.getOntClass(stoURI + "#" + "Relation");
	    ArrayList<String> relationList = new ArrayList<String>(); 
	    ExtendedIterator instances = Relation.listInstances();
	    
	    while (instances.hasNext()){
	    	Individual instance = (Individual) instances.next();
	    	relationList.add(instance.getURI());
	    }
	    
	    StmtIterator statementList = m.listStatements();
	    while (statementList.hasNext()) {
	    	Statement state = statementList.next();
	    	Resource s = state.getSubject();
	    	String subject = s.getURI();
//	    	System.out.println(subject + " : " + state.getPredicate().getURI() + " : " + state.getObject().toString());
	    	if (!state.getPredicate().getURI().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
	    	if (IsStringInArrayList(subject, relationList)) {
	    		subjectList.add(subject);
	    		System.out.println(subject);
	    	}
	    	}

	    }
	    
	    return subjectList;
	}
	
	public ArrayList<Triple> ReadTripleFromOnt(String inputFileName, String baseURI) {
		ArrayList<Triple> tripleList = new ArrayList<Triple>();
		
		OntModel m = ModelFactory.createOntologyModel();
	    InputStream in = FileManager.get().open(inputFileName);
	    
	    if (in == null) {
	        throw new IllegalArgumentException("File: " + inputFileName + " not found");
	    }
	    m.read(in, null);
	    
	    in = FileManager.get().open(STOL);
	    if (in == null) {
	        throw new IllegalArgumentException("File: " + STOL + " not found");
	    }
	    m.read(in, null);
	    
	    StmtIterator statementList = m.listStatements();
	    while (statementList.hasNext()) {
	    	Statement s = statementList.next();
	    	String predicate = s.getPredicate().getURI();
	    	String subject = s.getSubject().getURI();
	    	String object = s.getObject().toString();
	    	
//	    	predicate = predicate.substring(predicate.lastIndexOf("#")+1);
	    	if (predicate.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
	    		predicate = "rdf:type";
	    	}
	    	Triple t = new Triple();
	    	t.setSubject(subject);
	    	t.setPredicate(predicate);
	    	t.setObject(object);
	    	tripleList.add(t);
//    		System.out.println(predicate);
	    }
	    
	    
	    
	    return tripleList;
	}

	    
	    
}
