package neu.proposal.situation;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLObjectVisitorAdapter;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.util.SimpleRenderer;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.Reasoner;

public class SituationDerivation {
	
	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	static final String inputFolderName  = "/home/shanlu/Documents/ont";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String relationURI = "ScanSituation";
	
	static final String inferredFileName = "/home/shanlu/Documents/ont/bvrDump_maccdc_4.rdf";
	static final String situaationType = "";

	
	static final String base_stol  = "http://www.vistology.com/ont/2013/STO-L.owl";
	
	static final String outputFileName  = "/home/shanlu/Documents/ont/STIX_empty.owl";
	
	static ArrayList<OWLAxiom> axList = new ArrayList<OWLAxiom>();
	static ArrayList<OWLNamedIndividual> inList = new ArrayList<OWLNamedIndividual>();
	static ArrayList<OWLEquivalentClassesAxiom> eqaxList = new ArrayList<OWLEquivalentClassesAxiom>();
	static int addNumber = 0;
	static OWLObjectProperty objPInAxiom;
	
	public static void addRelevantFactsOld(OWLNamedIndividual in, OWLReasoner reasoner1, OWLOntology temp, OWLOntology out, OWLDataFactory dataFactory_out, OWLOntologyManager manager_output) throws OWLOntologyStorageException {
//		OWLOntologyManager manager_output = OWLManager.createOWLOntologyManager();
//		File file_output = new File(outputFileName);
//		File folder = new File(inputFolderName);
//		AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
//		manager_output.addIRIMapper(mapper);
//		SimpleIRIMapper iriMapper1 =  new SimpleIRIMapper(IRI.create("http://data-marking.mitre.org/DataMarking#"), IRI.create(new File("/home/shanlu/Documents/ont/DataMarking.owl")));
//		manager_output.addIRIMapper(iriMapper1);
//		SimpleIRIMapper iriMapper2 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/Common#"), IRI.create(new File("/home/shanlu/Documents/ont/CyboxCommon.owl")));
//		manager_output.addIRIMapper(iriMapper2);
//		SimpleIRIMapper iriMapper3 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/cybox_v1#"), IRI.create(new File("/home/shanlu/Documents/ont/Cybox.owl")));
//		manager_output.addIRIMapper(iriMapper3);
//		SimpleIRIMapper iriMapper4 =  new SimpleIRIMapper(IRI.create("http://maec.mitre.org/XMLSchema/maec-core-2#"), IRI.create(new File("/home/shanlu/Documents/ont/Maec.owl")));
//		manager_output.addIRIMapper(iriMapper4);
//		SimpleIRIMapper iriMapper5 =  new SimpleIRIMapper(IRI.create("http://capec.mitre.org/capec_v1#"), IRI.create(new File("/home/shanlu/Documents/ont/Capec.owl")));
//		manager_output.addIRIMapper(iriMapper5);
		
//		try {
//			OWLOntology out = manager_output.loadOntologyFromOntologyDocument(file_output);
//			System.out.println("Loaded ontology: " + out);
//			OWLDataFactory dataFactory_out = manager_output.getOWLDataFactory();
			
			addNumber++;
			System.out.println(addNumber);
//			ArrayList<OWLNamedIndividual> inList = new ArrayList<OWLNamedIndividual>();
			
			OWLNamedIndividual s = dataFactory_out.getOWLNamedIndividual(in.getIRI());
			Set<OWLClass> cs = reasoner1.getTypes(in, false).getFlattened();
			for (OWLClass c : cs) {
				OWLClass c_out = dataFactory_out.getOWLClass(c.getIRI());
				OWLAxiom ca = dataFactory_out.getOWLClassAssertionAxiom(c_out, s);
				System.out.println(ca);
				manager_output.addAxiom(out, ca);
				manager_output.saveOntology(out);
			}
			
			if (!inList.contains(in)) {
				Set<OWLDataPropertyAssertionAxiom> dataAxioms = temp.getDataPropertyAssertionAxioms(in);
				if (!dataAxioms.isEmpty()) {
					for (OWLDataPropertyAssertionAxiom dataAxiom : dataAxioms) {
						OWLDataProperty dp = dataAxiom.getProperty().asOWLDataProperty();
						OWLDataProperty dp_out = dataFactory_out.getOWLDataProperty(dp.getIRI());
						SimpleRenderer renderer = new SimpleRenderer();
						for (OWLLiteral lit : EntitySearcher.getDataPropertyValues(in, dp, temp)) {
	//					    System.out.println(in + " has values " + renderer.render(lit));
						    OWLAxiom dAxiom = dataFactory_out.getOWLDataPropertyAssertionAxiom(dp_out, s, lit);
						    if (!axList.contains(dAxiom)) {
						    	manager_output.addAxiom(out, dAxiom);
						    	manager_output.saveOntology(out);
						    	System.out.println(dAxiom);
						    }
						}
					}
				}
				
				Set<OWLObjectPropertyAssertionAxiom> obAxioms = temp.getObjectPropertyAssertionAxioms(in);
				if (obAxioms.isEmpty()) {
					return;
				} 
				else {
					for (OWLObjectPropertyAssertionAxiom obAxiom : obAxioms) {
						OWLObjectProperty p = obAxiom.getProperty().asOWLObjectProperty();
						OWLObjectProperty p_out = dataFactory_out.getOWLObjectProperty(p.getIRI());
						OWLIndividual d = obAxiom.getObject();
						OWLNamedIndividual dd = d.asOWLNamedIndividual();
						OWLIndividual d_out = dataFactory_out.getOWLNamedIndividual(dd.getIRI());
						OWLAxiom a = dataFactory_out.getOWLObjectPropertyAssertionAxiom(p_out, s, d_out);
						if (!axList.contains(a)) {
							manager_output.addAxiom(out, a);
							manager_output.saveOntology(out);
							System.out.println(a);
							axList.add(a);
							addRelevantFactsOld(dd, reasoner1, temp, out, dataFactory_out, manager_output);
						}
					}
					manager_output.saveOntology(out);
				}
				inList.add(in);
			}
			else {
				return;
			}
			
//		} catch (OWLOntologyInputSourceException | OWLOntologyCreationException ex) {
//            // throw custom exception
//        }
		
	}
	
	public static void addRelevantFacts(OWLNamedIndividual in, OWLEquivalentClassesAxiom eqAxiom, OWLReasoner reasoner1, OWLOntology temp, OWLOntology out, OWLDataFactory dataFactory_out, OWLOntologyManager manager_output) throws OWLOntologyStorageException {
		
		OWLClassExpressionVisitor vistor = new OWLClassExpressionVisitorAdapter () {
			@Override
	         public void visit(OWLObjectIntersectionOf ce) {
	                for (OWLClassExpression e : ce.getOperands()) {
	                	System.out.println("Visit ObjectIntersectionOf ------");
						System.out.println(e);
	                    e.accept(this);
	                }
	            }
//			@Override
//	         public void visit(OWLObjectUnionOf ce) {
//	                for (OWLClassExpression e : ce.getOperands()) {
//	                	System.out.println("Visit ObjectUnionOf ------");
//						System.out.println(e);
//	                    e.accept(this);
//	                }
//	            }
			@Override
		    public void visit(OWLObjectSomeValuesFrom ce) {
		        OWLObjectPropertyExpression p = ce.getProperty();
		        objPInAxiom = p.asOWLObjectProperty();
		        System.out.println("ObjectProperty in ClassesAxiom ------");
		        System.out.println(p);
		        // here you can do what you need with the property.
		        OWLClassExpression c = ce.getFiller();
		        System.out.println("Class in ClassesAxiom ------");
		        System.out.println(c);
		        c.accept(this);
		    }
			 @Override
			 public void visit(OWLObjectHasValue ce) {
				 OWLObjectPropertyExpression p = ce.getProperty();
				 System.out.println("ObjectProperty in ClassesAxiom ------");
			     System.out.println(p);
			     OWLIndividual ind = ce.getFiller();
			     System.out.println("Instance in ClassesAxiom ------");
			     System.out.println(ind);
			 }
//			 @Override
//			 public void visit(OWLDataHasValue ce) {
//				 OWLDataPropertyExpression p = ce.getProperty();
//				 System.out.println("DataProperty in ClassesAxiom ------");
//			     System.out.println(p);
//			     OWLLiteral ind = ce.getFiller();
//			     System.out.println("Literal in ClassesAxiom ------");
//			     System.out.println(ind);
//			 }
		};
		
		Set<OWLClassExpression> expressions = eqAxiom.getClassExpressions();
		for (OWLClassExpression e : expressions) {
			 e.accept(vistor);
		}
		
		Set<OWLObjectPropertyAssertionAxiom> obAxioms = temp.getObjectPropertyAssertionAxioms(in);
		if (obAxioms.isEmpty()) {
			return;
		} 
		else {
			for (OWLObjectPropertyAssertionAxiom obAxiom : obAxioms) {
				OWLObjectProperty p = obAxiom.getProperty().asOWLObjectProperty();
				OWLObjectProperty p_out = dataFactory_out.getOWLObjectProperty(p.getIRI());
				OWLIndividual d = obAxiom.getObject();
				OWLNamedIndividual dd = d.asOWLNamedIndividual();
				OWLIndividual d_out = dataFactory_out.getOWLNamedIndividual(dd.getIRI());
				OWLAxiom a = dataFactory_out.getOWLObjectPropertyAssertionAxiom(p_out, in, d_out);
				if (!axList.contains(a)) {
					manager_output.addAxiom(out, a);
					manager_output.saveOntology(out);
					System.out.println(a);
					axList.add(a);
					addRelevantFactsOld(dd, reasoner1, temp, out, dataFactory_out, manager_output);
				}
			}
			manager_output.saveOntology(out);
		}
		inList.add(in);
	}
	
	public static void DeriveSituation(String situationName) throws OWLOntologyCreationException, OWLOntologyStorageException {
		
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntologyManager manager_inferred = OWLManager.createOWLOntologyManager();
		OWLOntologyManager manager_output = OWLManager.createOWLOntologyManager();
		
		File file = new File(inputFileName);
		File file_inferred = new File(inferredFileName);
		File file_output = new File(outputFileName);
		
		File folder = new File(inputFolderName);
		
		AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
		manager.addIRIMapper(mapper);
		manager_output.addIRIMapper(mapper);
		
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
		
		SimpleIRIMapper iriMapper6 =  new SimpleIRIMapper(IRI.create("http://data-marking.mitre.org/DataMarking#"), IRI.create(new File("/home/shanlu/Documents/ont/DataMarking.owl")));
		manager_output.addIRIMapper(iriMapper6);
		SimpleIRIMapper iriMapper7 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/Common#"), IRI.create(new File("/home/shanlu/Documents/ont/CyboxCommon.owl")));
		manager_output.addIRIMapper(iriMapper7);
		SimpleIRIMapper iriMapper8 =  new SimpleIRIMapper(IRI.create("http://cybox.mitre.org/cybox_v1#"), IRI.create(new File("/home/shanlu/Documents/ont/Cybox.owl")));
		manager_output.addIRIMapper(iriMapper8);
		SimpleIRIMapper iriMapper9 =  new SimpleIRIMapper(IRI.create("http://maec.mitre.org/XMLSchema/maec-core-2#"), IRI.create(new File("/home/shanlu/Documents/ont/Maec.owl")));
		manager_output.addIRIMapper(iriMapper9);
		SimpleIRIMapper iriMapper10 =  new SimpleIRIMapper(IRI.create("http://capec.mitre.org/capec_v1#"), IRI.create(new File("/home/shanlu/Documents/ont/Capec.owl")));
		manager_output.addIRIMapper(iriMapper10);
		
		long start = System.currentTimeMillis();
		
		System.out.println(start);
		try {
			OWLOntology stix = manager.loadOntologyFromOntologyDocument(file);
			System.out.println("Loaded ontology: " + stix);
			
			OWLOntology temp = manager_inferred.loadOntologyFromOntologyDocument(file_inferred);
			System.out.println("Loaded ontology: " + temp);
			
			OWLOntology out = manager_output.loadOntologyFromOntologyDocument(file_output);
			System.out.println("Loaded ontology: " + out);
			
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			OWLDataFactory dataFactory_inferred = manager_inferred.getOWLDataFactory();
			OWLDataFactory dataFactory_out = manager_output.getOWLDataFactory();
			
			//extract the axiom of the situation type definition
			
			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			OWLReasoner reasoner = reasonerFactory.createReasoner(stix);
			reasoner.precomputeInferences();
			OWLReasoner reasoner1 = reasonerFactory.createReasoner(temp);
			reasoner1.precomputeInferences();
			
//			boolean consistent = reasoner.isConsistent();
//			Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
//			Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
//	        if (!unsatisfiable.isEmpty()) {
//	             System.out.println("The following classes are unsatisfiable: ");
//	            for (OWLClass cls : unsatisfiable) {
//	                 System.out.println(" " + cls);
//	            }
//	        } else {
//	             System.out.println("There are no unsatisfiable classes");
//	        }
	        
	        
	        
	        OWLClass scanSituation = dataFactory.getOWLClass(IRI.create(base + "#BufferOverflowSituation"));
			String situationType_axiom = stix.getEquivalentClassesAxioms(scanSituation).toString();
//			System.out.println(situationType_axiom);
			
			Set<OWLEquivalentClassesAxiom> axioms = stix.getEquivalentClassesAxioms(scanSituation);
//			OWLObjectProperty supports = dataFactory.getOWLObjectProperty(IRI.create(base_stol + "#supports"));
//			OWLObjectProperty relationP = dataFactory.getOWLObjectProperty(IRI.create(base_stol + "#relation"));
//			OWLObjectProperty anchor1 = dataFactory.getOWLObjectProperty(IRI.create(base_stol + "#anchor1"));
//			OWLObjectProperty anchor2 = dataFactory.getOWLObjectProperty(IRI.create(base_stol + "#anchor2"));
//			OWLObjectProperty anchor3 = dataFactory.getOWLObjectProperty(IRI.create(base_stol + "#anchor3"));
//			OWLClass infon = dataFactory.getOWLClass(IRI.create(base_stol + "#Infon"));
			OWLObjectProperty relevantIndividual = dataFactory.getOWLObjectProperty(IRI.create(base_stol + "#relevantIndividual"));
			OWLObjectProperty relevantRelation = dataFactory.getOWLObjectProperty(IRI.create(base_stol + "#relevantRelation"));
			
			

			

			
//			for (OWLEquivalentClassesAxiom a : axioms) {
//			Set<OWLClassExpression> expressions = a.getClassExpressions();
//			for (OWLClassExpression e : expressions) {
//			     e.accept(vistor);
//				}
//			}
			
			
			String relation;
			String[] anchor_type = new String[2];
			String[] anchor = new String[2];
			
			//analyze the axiom 
			//1. extract the relation
			String[] parts = situationType_axiom.split("#relation>");
			relation = parts[1].substring(parts[1].indexOf("<")+1);
			relation = relation.substring(0, relation.indexOf(">"));
			System.out.println(relation);
			
			OWLObjectProperty re = dataFactory_inferred.getOWLObjectProperty(IRI.create(relation));
//			OWLObjectProperty re = dataFactory_out.getOWLObjectProperty(IRI.create(relation));
			
			//2. extract anchors
			String[] parts1 = situationType_axiom.split("#anchor1>");
			anchor_type[0] = parts1[0].substring(parts1[0].lastIndexOf(">")+2, parts1[0].lastIndexOf("("));
//			System.out.println(anchor_type[0]);
			anchor[0] = parts1[1].substring(parts1[1].indexOf("<")+1, parts1[1].indexOf(">"));
//			System.out.println(anchor[0]);
			
			String[] parts2 = situationType_axiom.split("#anchor2>");
			anchor_type[1] = parts2[0].substring(parts2[0].lastIndexOf(">")+3, parts2[0].lastIndexOf("("));
//			System.out.println(anchor_type[1]);
			anchor[1] = parts2[1].substring(parts2[1].indexOf("<")+1, parts2[1].indexOf(">"));
//			System.out.println(anchor[1]);
			
			OWLClass anc1 = dataFactory.getOWLClass(IRI.create(anchor[0]));
			OWLClass anc2 = dataFactory.getOWLClass(IRI.create(anchor[1]));
			
			Set<OWLEquivalentClassesAxiom> eqAxioms1 = stix.getEquivalentClassesAxioms(anc1);
//			if (!eqAxioms1.isEmpty()){
//				for (OWLEquivalentClassesAxiom eqAxiom : eqAxioms1) {
//					eqaxList.add(eqAxiom);
//					System.out.println(eqAxioms1);
//					Set<OWLClassExpression> expressions = eqAxiom.getClassExpressions();
//					for (OWLClassExpression e : expressions) {
//						 e.accept(vistor);
//					}
//				}
//			}
			Set<OWLEquivalentClassesAxiom> eqAxioms2 = stix.getEquivalentClassesAxioms(anc2);
//			if (!eqAxioms2.isEmpty()){
//				for (OWLEquivalentClassesAxiom eqAxiom : eqAxioms2) {
//					eqaxList.add(eqAxiom);
//					System.out.println(eqAxioms2);
//					Set<OWLClassExpression> expressions = eqAxiom.getClassExpressions();
//					for (OWLClassExpression e : expressions) {
//						 e.accept(vistor);
//					}
//				}
//			}		
			
			int i = 0;
			
			
			
			//check 
			if (anchor_type[0].equals("ObjectSomeValuesFrom") && anchor_type[1].equals("ObjectSomeValuesFrom")){
//				OWLClass anchor1 = dataFactory_inferred.getOWLClass(IRI.create(anchor[0]));
//				OWLClass anchor2 = dataFactory_inferred.getOWLClass(IRI.create(anchor[1]));
				OWLClass anchor1 = dataFactory_out.getOWLClass(IRI.create(anchor[0]));
				OWLClass anchor2 = dataFactory_out.getOWLClass(IRI.create(anchor[1]));
				NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner1.getInstances(anchor1, false);
		        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
		        for (OWLNamedIndividual in : individuals) {
		        	OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(in.getIRI());
		        	Set<OWLNamedIndividual> values = reasoner1.getObjectPropertyValues(in, re).getFlattened();
		        	for (OWLNamedIndividual v : values) {
//		        		OWLNamedIndividual va = dataFactory_inferred.getOWLNamedIndividual(v.getIRI());
//		        		Set<OWLClassAssertionAxiom> cAxioms = temp.getClassAssertionAxioms(va);
		        		OWLNamedIndividual va = dataFactory_out.getOWLNamedIndividual(v.getIRI());
		        		Set<OWLClassAssertionAxiom> cAxioms = out.getClassAssertionAxioms(va);
		        		for (OWLClassAssertionAxiom a : cAxioms) {
		        			Set<OWLClass> cs = a.getClassesInSignature();
		        			for (OWLClass c : cs) {
		        				if (c.equals(anchor2)) {
		        					OWLIndividual s = dataFactory.getOWLNamedIndividual(IRI.create(base + "#s" + i));
		        					
		        					OWLAxiom situationA = dataFactory.getOWLClassAssertionAxiom(scanSituation, s);
		        					manager.addAxiom(stix, situationA);
		        					OWLAxiom relInd1 = dataFactory.getOWLObjectPropertyAssertionAxiom(relevantIndividual, s, ind);
		        					manager.addAxiom(stix, relInd1);
		        					OWLAxiom relInd2 = dataFactory.getOWLObjectPropertyAssertionAxiom(relevantIndividual, s, v);
		        					manager.addAxiom(stix, relInd2);
		        					OWLIndividual reInd = dataFactory.getOWLNamedIndividual(IRI.create(relation));
		        					OWLAxiom relRel = dataFactory.getOWLObjectPropertyAssertionAxiom(relevantRelation, s, reInd);
		        					manager.addAxiom(stix, relRel);
		        					i++;
		        					manager.saveOntology(stix);
		        					
//		        					System.out.println("------------Start Relevant Facts-----------------");
		        					
//		        					OWLAxiom ind1Clas = dataFactory_out.getOWLClassAssertionAxiom(anchor1, ind);
//		        					OWLAxiom ind2Clas = dataFactory_out.getOWLClassAssertionAxiom(anchor2, v);
//		        					OWLAxiom triple = dataFactory_out.getOWLObjectPropertyAssertionAxiom(re, ind, v);
//		        					manager_output.addAxiom(out, ind1Clas);
//		        					manager_output.addAxiom(out, ind2Clas);
//		        					manager_output.addAxiom(out, triple);
//		        					manager_output.saveOntology(out);
//		        					
//		        					if (!eqAxioms1.isEmpty()){
//		        						System.out.println("------------Start Relevant Facts in Anchor1-----------------");
//		        						for (OWLEquivalentClassesAxiom eqAxiom : eqAxioms1) {
//		        							addRelevantFacts(in,eqAxiom,reasoner1,temp,out, dataFactory_out, manager_output);
//		        						}
//		        					}
//		        					if (!eqAxioms2.isEmpty()){
//		        						System.out.println("------------Start Relevant Facts in Anchor2-----------------");
//		        						for (OWLEquivalentClassesAxiom eqAxiom : eqAxioms2) {
//		        							addRelevantFacts(v,eqAxiom,reasoner1,temp,out, dataFactory_out, manager_output);
//		        						}
//		        					}
		        				}
		        			}
		        		}
		                 
		        	}
		        }
				
			}
			
			
			
			
//			else if (anchor_type[0].equals("ObjectHasValue") && anchor_type[1].equals("ObjectHasValue")){
//				OWLIndividual anchor1 = dataFactory_inferred.getOWLNamedIndividual(IRI.create(anchor[0]));
//				OWLIndividual anchor2 = dataFactory_inferred.getOWLNamedIndividual(IRI.create(anchor[1]));
//			}
			
			
			
		    

		} catch (OWLOntologyInputSourceException | OWLOntologyCreationException ex) {
            // throw custom exception
        }
		
		
		System.out.println(System.currentTimeMillis() - start);
	}

}
