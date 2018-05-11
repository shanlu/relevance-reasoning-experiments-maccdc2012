package neu.proposal.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.IntersectionClass;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

import com.google.common.base.Optional;

public class Map {
	
	static final String inputFileName  = "C:/Users/shanl/Documents/STO-L.owl";
	static final String baseURI = "http://www1.coe.neu.edu/~shanlu/research/ont/STO-L.owl";

	static final File f = new File("C:/Users/shanl/Documents/ex001.rq");

	
	private static List<String> readRQFile(File fin) throws IOException {
		// Construct BufferedReader from FileReader
		BufferedReader br = new BufferedReader(new FileReader(fin));
	 
		String queryString = "";
		List<String> queryFile = new ArrayList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			queryFile.add(line.trim());
			queryString = queryString + line + " ";
		}
//		System.out.println(queryString);
		br.close();
		
		return queryFile;
	}
	
	private static HashMap getVariables(List<String> queryFile) {
		HashMap<String, String> variableMap = new HashMap<String, String>();
		
		Iterator<String> iterator = queryFile.iterator();
		while (iterator.hasNext()) {
			String line = iterator.next();
			if (line.contains("rdf:type") && line.contains("?")) {
				String[] elements = line.split(" ");
				for (int i=0; i<elements.length;i++) {
					elements[i] = elements[i].replace(".", "");
				}
				for (int i=0; i<elements.length;i++) {
					if (elements[i].contains("?")) {
						variableMap.put(elements[i], elements[i+2]);
//						System.out.println(elements[i] + " is a " + elements[i+2]);
					}
				}
			}
		}
		
		return variableMap;
	}
	
	private static HashMap getRelation(List<String> queryFile) {
//		List<String> relations = new ArrayList<String>();
		
		HashMap<String, Relation> relationMap = new HashMap<String, Relation>();
		HashMap<String, String> variableMap = getVariables(queryFile);
		Set<Entry<String, String>> entrySet = variableMap.entrySet();
		
		Iterator it = entrySet.iterator();
		while(it.hasNext()){
			Entry me = (Entry)it.next();
			System.out.println("Key is: " + me.getKey() + " & " + "Value is: " + me.getValue());
		}

		Iterator<String> iterator = queryFile.iterator();
		while (iterator.hasNext()) {
			String line = iterator.next();
			if (line.contains("ex:par")) {
				String[] elements = line.split(" ");
				for (int i=0; i<elements.length;i++) {
					elements[i] = elements[i].replace(".", "");
				}
				for (int i=0; i<elements.length;i++) {
					if (elements[i].contains("ex:par")) {
						if (!relationMap.containsKey(elements[i-1])) {
							Relation r = new Relation();
							r.setName(elements[i-1]);
							System.out.println(r.name);
							List<Argument> argumentList = new ArrayList<Argument>();
							Argument a = new Argument();
							a.setDomain(r);
							if (!elements[i+1].contains("?")) {
								a.setName(elements[i+1]);
								argumentList.add(a);
								System.out.println(a.name);
							}
							else {
								String classType = variableMap.get(elements[i+1]);
								a.setClassType(classType);
								argumentList.add(a);
								System.out.println(a.classType);
							}
							
							r.setArgumentList(argumentList);
							relationMap.put(elements[i-1], r);
						}
						else {
							Relation r = relationMap.get(elements[i-1]);
							Argument a = new Argument();
							a.setDomain(r);
							if (!elements[i+1].contains("?")) {
								a.setName(elements[i+1]);
								System.out.println(a.name);
							}
							else {
								String classType = variableMap.get(elements[i+1]);
								a.setClassType(classType);
								System.out.println(a.classType);
							}
							r.getArgumentList().add(a);
						}
					}
				}
				
			}
		}	
		
		return relationMap;
	}
	
	private static void createInfon(HashMap<String, Relation> relationMap) {
		OntModel m = ModelFactory.createOntologyModel();
	    InputStream in = FileManager.get().open(inputFileName);
	    if (in == null) {
	        throw new IllegalArgumentException("File: " + inputFileName + " not found");
	    }
	    m.read(in, null);
	    
	    OntProperty relation = m.getOntProperty(baseURI + "#" + "relation");
	    OntProperty supports = m.getOntProperty(baseURI + "#" + "supports");
		OntClass infon = m.getOntClass(baseURI + "#" + "Infon");
		
		OntClass situationType = m.createClass(baseURI + "#" + "SituationType");
		OntClass situation = m.getOntClass(baseURI + "#" + "Situation");
		situation.addSubClass(situationType); 
		
		Set<Entry<String, Relation>> entrySet = relationMap.entrySet();
		Iterator it = entrySet.iterator();
		int i = 0;
		//for each relation
		while(it.hasNext()){
			Entry me = (Entry)it.next();
			Relation r = (Relation)me.getValue();
			
			String relationName = r.name.substring(r.name.indexOf(":")+1);
			System.out.println(relationName);
			Individual rela = m.getIndividual(baseURI + "#" + relationName);
			
		    OntClass infon1 = m.createClass(baseURI + "#" + "Infon" + i);
			i++;
			System.out.println(i);
			
			infon.addSubClass(infon1);
			situationType.addSuperClass(m.createSomeValuesFromRestriction(null, supports, infon1));
			
			infon1.addSuperClass(m.createHasValueRestriction( null, relation, rela));
			
			List<Argument> argumentList = r.getArgumentList();
		    System.out.println("Key is: " + me.getKey());
		    Iterator<Argument> iterator = argumentList.iterator();
		    int j=1;
		    	// for each argument in the relation
				while (iterator.hasNext()) {
					Argument a = iterator.next();
					Property anchor = m.getProperty(baseURI + "#" + "anchor" + j);
					j++;
					if (a.name == null) {
						String typeName = a.classType.substring(a.classType.indexOf(":")+1);
						OntClass type = m.createClass(baseURI + "#" + typeName);
						infon1.addSuperClass(m.createSomeValuesFromRestriction(null, anchor, type));
					}
					else {
						String name = a.name.substring(a.name.indexOf("?")+1);
						Individual arg = m.createIndividual(baseURI + "#" + name, infon1);
						infon1.addSuperClass(m.createHasValueRestriction( null, anchor, arg));
					}
					
					System.out.println(a.getClassType());
				}
				
				ExtendedIterator<OntClass> ei = infon1.listSuperClasses(true);
				RDFList eqclasses = m.createList(ei);
				IntersectionClass eq = m.createIntersectionClass(null, eqclasses);
				infon1.addEquivalentClass(eq);
		}
		
		
		
		ExtendedIterator<OntClass> subST = situationType.listSuperClasses(true);
		RDFList eqclasses = m.createList(subST);
		IntersectionClass eq = m.createIntersectionClass(null, eqclasses);
		situationType.addEquivalentClass(eq);
		
		FileWriter out=null;
	    try {
	        out = new FileWriter( inputFileName );
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    try 
	    {
	        m.write( out, "RDF/XML" );
	    }
	    finally 
	    {
	       try 
	       {
	           out.close();
	       }
	       catch (IOException closeException) 
	       {
	           // ignore
	       }

	    }
	}

	
	public static void main(String[] args) {
		
		List<String> queryFile = new ArrayList<String>();
		
		try {
			queryFile = readRQFile(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HashMap<String, Relation> relationMap = getRelation(queryFile);
		
		createInfon(relationMap);
		

	}

}
