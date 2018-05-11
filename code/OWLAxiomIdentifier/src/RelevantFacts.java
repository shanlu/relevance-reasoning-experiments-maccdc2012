package neu.proposal.axiom;

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


public class RelevantFacts {
	
	static final String inputFileName  = "/home/shanlu/Documents/ont/STIX.owl";
	static final String inputFolderName  = "/home/shanlu/Documents/ont";
	static final String base  = "http://stix.mitre.org/STIX";
	static final String relationURI = "";
	
	public static boolean IsStringInArrayList(String goal, ArrayList<String> predicateList) {
		Iterator predicates = predicateList.iterator();
		
		while (predicates.hasNext()) {
			if (predicates.next().equals(goal)) {
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
		
		
		
		try {
			OWLOntology stix = manager.loadOntologyFromOntologyDocument(file);
//			System.out.println("Loaded ontology: " + stix);
			
			IRI documentIRI = manager.getOntologyDocumentIRI(stix);
//			System.out.println(" from: " + documentIRI);
			
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			OWLObjectProperty scans = dataFactory.getOWLObjectProperty(
					IRI.create(base + "#scans"));
			OWLObjectProperty connects = dataFactory.getOWLObjectProperty(
					IRI.create(base + "#connects"));
			
//			System.out.println(stix.getObjectSubPropertyAxiomsForSuperProperty(scans).toString());
//			System.out.println(stix.getObjectSubPropertyAxiomsForSubProperty(connects).toString());
			
//			String subAxiom1 = stix.getObjectSubPropertyAxiomsForSubProperty(connects).toString();
//			System.out.println(subAxiom1);
	
			
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
				
				OWLObjectProperty superRelation = dataFactory.getOWLObjectProperty(
						IRI.create(matchList.get(1)));
			}
			
			
			
			
		} catch (OWLOntologyInputSourceException | OWLOntologyCreationException ex) {
            // throw custom exception
        }
		
		
		
	}
	
	

}
