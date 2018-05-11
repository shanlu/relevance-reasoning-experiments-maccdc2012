package neu.proposal.axiom;

import java.io.File;
import java.io.InputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;




public class ReadOWLAxiom {
	
	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	static final String baseURI = "http://stix.mitre.org/STIX";
	
	static final String Goal = "http://stix.mitre.org/STIX#scans";
	
	public static void main(String argv[]) {
	
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File(inputFileName);
		try {
			OWLOntology localPizza = manager.loadOntologyFromOntologyDocument(file);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
