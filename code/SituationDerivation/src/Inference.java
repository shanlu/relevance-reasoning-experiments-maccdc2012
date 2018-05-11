package neu.proposal.situation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.io.OWLOntologyInputSourceException;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class Inference {
	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	static final String inputFolderName  = "/home/shanlu/Documents/ont";
	static final String base  = "http://stix.mitre.org/STIX";
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
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
			
			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			OWLReasoner reasoner = reasonerFactory.createReasoner(stix);
			reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
			
			List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
	        gens.add(new InferredClassAssertionAxiomGenerator());
	        
	        OWLOntology infOnt = manager.createOntology();
	        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
	        iog.fillOntology(manager.getOWLDataFactory(), infOnt);
			
	        manager.saveOntology(infOnt, new RDFXMLDocumentFormat(), IRI.create(new File("/home/shanlu/Documents/ont/iferred_api.owl")));
			
		} catch (OWLOntologyInputSourceException | OWLOntologyCreationException ex) {
            // throw custom exception
        }
		
	}

}
