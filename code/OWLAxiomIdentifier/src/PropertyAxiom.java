package neu.proposal.axiom;

import java.io.File;
import java.util.Set;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.Reasoner;

public class PropertyAxiom {
	
	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	static final String inputFolderName  = "/home/shanlu/Documents/ont";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String relationURI = "";
	
	
	
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
		
		
		try {
			OWLOntology stix = manager.loadOntologyFromOntologyDocument(file);
			System.out.println("Loaded ontology: " + stix);
			
			IRI documentIRI = manager.getOntologyDocumentIRI(stix);
			System.out.println(" from: " + documentIRI);
			
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			OWLClass scanSituation = dataFactory.getOWLClass(IRI.create(base + "#ScanSituation"));
			
			System.out.println(scanSituation.toString());
			
			System.out.println(stix.getEquivalentClassesAxioms(scanSituation));
			
			OWLIndividual event = dataFactory.getOWLNamedIndividual(IRI.create(base + "#snort2.1.2Event0"));
			
			OWLObjectProperty indicator = dataFactory.getOWLObjectProperty(IRI.create(base + "#indicator"));
			
			Set<OWLObjectPropertyAssertionAxiom> inds = stix.getObjectPropertyAssertionAxioms(event);
		    for (OWLObjectPropertyAssertionAxiom ind: inds){
		        System.out.println(ind);
		    }
		    
		    OWLOntology o = manager.loadOntologyFromOntologyDocument(IRI.create(base));
		    
//		    Reasoner hermit=new Reasoner(o);
//		    System.out.println(hermit.isConsistent());
		    
		    PelletReasoner reasoner = com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory.getInstance().createReasoner(o);
		    System.out.println(reasoner.isConsistent());
			

//			
//			Set<OWLNamedIndividual> inds = stix.getIndividualsInSignature();
//		    for (OWLNamedIndividual ind: inds){
//		        System.out.println(ind.getObjectPropertyValues(dataFactory.getOWLObjectProperty(IRI.create(base + "#indicator")), stix));
//		    }
			
			
//			OWLObjectProperty scans = dataFactory.getOWLObjectProperty(
//					IRI.create(base + "#scans"));
//			OWLObjectProperty connects = dataFactory.getOWLObjectProperty(
//					IRI.create(base + "#connects"));
//			
//			System.out.println(stix.getObjectSubPropertyAxiomsForSuperProperty(scans));
//			System.out.println(stix.getObjectSubPropertyAxiomsForSubProperty(connects));
//			
//			System.out.println(stix.getTransitiveObjectPropertyAxioms(scans));
//			if (stix.getTransitiveObjectPropertyAxioms(scans).isEmpty()) {
//				System.out.println("ok");
//			}
//			
			
		} catch (OWLOntologyInputSourceException | OWLOntologyCreationException ex) {
            // throw custom exception
        }
		
		
		
	}

}
