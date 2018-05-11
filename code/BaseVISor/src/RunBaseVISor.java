package edu.neu.ece.concerto.bvrjava;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vistology.bvr.BaseException;
import com.vistology.bvr.BaseFactory;
import com.vistology.bvr.Configuration;
import com.vistology.bvr.Fact;
import com.vistology.bvr.Observer;
import com.vistology.bvr.compile.DocumentCompiler;
import com.vistology.bvr.compile.DocumentCompiler.CompilationResult;
import com.vistology.bvr.compile.NullFactory;
import com.vistology.bvr.rete.ReteEvent;
import com.vistology.bvr.rete.ReteNet;
import com.vistology.bvr.store.FactBase;
import com.vistology.bvr.store.PlainFactBase;
import com.vistology.bvr.thing.AbbrMap;
import com.vistology.bvr.thing.Thing;
import com.vistology.bvr.thing.ThingType;
import com.vistology.bvr.util.BaseLogger;

public class RunBaseVISor implements Observer {

	private static final String CONFIG_PATH = "conf" + File.separator;
	private static final String BVR_CONFIG_PATH = CONFIG_PATH + "bvrconfig.xml";
	private final static String CRO_URI = "http://ece.neu.edu.crf/OBROntology.owl";
	private final static String STOL_URI = "http://www.vistology.com/ont/2013/STO-L.owl";
	private final static String ECEONTS_URI = "http://ece.neu.edu/ontologies/";
	private final static String ONTS_DIR_NAME = ".." + File.separator + "Ontology";
	private final static String INFERRED_BLANK_NS_PREFIX = "http://blank-node/id-";
	private final static Pattern FIND_BLANK_PATTERN = Pattern.compile("\\\"#\\(blank_([0-9]+)\\)\\\"");
	private final static boolean UNBLANK_RDF = true;
	private final static boolean PRETTY_RDF = true;

	private long blankCounter;
	private DocumentCompiler compiler;
	private ReteNet rete;
	private final BaseLogger log = new BaseLogger(ReteNet.class);
	private final Logger logger = LoggerFactory.getLogger(getClass().getName());
	private String rulesPath;
	private String dumpedFilePath;

	static {
		PropertyConfigurator.configure(CONFIG_PATH + "log4j.properties");
		File f = new File(ONTS_DIR_NAME);
		if (f == null || !f.isDirectory() || !f.canRead()) {
			throw new IllegalStateException("Cannot load ontologies from " + ONTS_DIR_NAME);
		}
	}

	public RunBaseVISor(String rulesPath, String dumpedFilePath) {
		blankCounter = 0;
		if (rulesPath == null || rulesPath.isEmpty()) {
			throw new IllegalArgumentException("Cannot run BaseVISor without a rules file.");
		}
		File f = new File(rulesPath);
		if (f == null || !f.canRead()) {
			throw new IllegalArgumentException("Cannot read the rules file from " + rulesPath);
		}
		this.rulesPath = rulesPath;
		this.dumpedFilePath = dumpedFilePath;
	}

	private void initialize() throws Exception {
		Configuration config = new Configuration(BVR_CONFIG_PATH);
		compiler = new DocumentCompiler(config, getURIMapping(), this, 0, log);
		compiler.register(new ProceduralAttachments(compiler.getThingFactory()));
		logger.debug("Compiling the following rules file:{}", rulesPath);
		CompilationResult compResult = compiler.compile(rulesPath);
		BaseFactory baseFactory = compResult.getBaseFactory();
		if (baseFactory == null || baseFactory instanceof NullFactory)
			throw new RuntimeException("Failed to process the rules document.");
		rete = compResult.getRete() == null ? baseFactory.constructReteNet() : compResult.getRete();
	}


	private HashMap<String, String> getURIMapping() {
		HashMap<String, String> uriMap = new HashMap<>();
//		uriMap.put(ECEONTS_URI + "ConcertOlogy_ABox.owl", getOntPath("ConcertOlogy_ABox.owl"));
//		uriMap.put(ECEONTS_URI + "ConcertOlogy.owl", getOntPath("ConcertOlogy2.owl"));
		uriMap.put(ECEONTS_URI + "STIX.owl", getOntPath("STIX.owl"));
		uriMap.put(STOL_URI, getOntPath("STO-L.owl"));
		logger.debug("URI Mapping:\n{}", uriMap);
		return uriMap;
	}
	
	private static String getOntPath(String ontFileName) {
		File ontFile = new File(ONTS_DIR_NAME + File.separator + ontFileName);
		if (!ontFile.canRead()) {
			throw new IllegalStateException(
					"Cannot read the ontology file: ./" + ONTS_DIR_NAME + File.pathSeparator + ontFileName);
		}
		return "file:" + ontFile.getAbsolutePath();
	}


	public String toRDF() {
		StringBuilder sb = new StringBuilder();
		sb.append("<rdf:RDF\n");
		AbbrMap abbreviations = rete.getAbbreviations();
		abbreviations.displayGlobalDeclarations(sb, "  ");
		sb.append(">");
		if (PRETTY_RDF) {
			sb.append(filterRete(rete).toRDF(abbreviations, rete.getThingFactory()));
		} else {
			sb.append(filterRete(rete).toRDFFast(abbreviations, rete.getThingFactory()));
		}
		sb.append("</rdf:RDF>\n");

		String rdf = sb.toString();
		return UNBLANK_RDF ? unblank(rdf) : rdf;
	}

	/**
	 * Replace all #(blank_no) with resources so that the produced document is
	 * properly handled by Jena.
	 * 
	 * @param rdf
	 *            RDF/XML document
	 * @return RDF document without the blank nodes, which can be inserted into the
	 *         triple store
	 */
	private String unblank(String rdf) {
		logger.info("Removing blanks from the RDF dump.");
		if (StringUtils.isBlank(rdf)) {
			return rdf;
		}
		Matcher m = FIND_BLANK_PATTERN.matcher(rdf);
		Set<String> blanks = new HashSet<String>();
		if (m.find()) {
			blanks.add(m.group(0));
			while (m.find()) {
				blanks.add(m.group(0));
			}
			logger.trace("Blanks in the document for the triple store: {}", blanks);
			for (String blank : blanks) {
				String replacement = "\"" + INFERRED_BLANK_NS_PREFIX + (blankCounter++) + "\"";
				logger.trace("Replacing {} with {}", blank, replacement);
				rdf = StringUtils.replace(rdf, blank, replacement);
			}
		} else {
			logger.trace("No blanks in the document for the triple store.");
		}

		return rdf;
	}

	/**
	 * TODO You can remove other uninteresting facts here.
	 * 
	 * @param net
	 *            ReteNet after inference
	 * @return The same facts as in Rete, but skip internal triples
	 */
	private FactBase filterRete(ReteNet net) {
		FactBase facts = new PlainFactBase();
		for (Fact f : net.getFacts()) {
			Thing s = f.getSubject();
			Thing p = f.getPredicate();
			Thing o = f.getObject();
			String ss = s.toString();
			String ps = p.toString();
			String os = o.toString();

			if (ps.startsWith("#_") || ps.contains("_matchedClasses") || ps.contains("_sublist")) {
				continue;
			}

			if (s.getType().equals(ThingType.BLANK_ASSET)) {
				continue;
			}

			if (p.getType().equals(ThingType.BLANK_ASSET)) {
				continue;
			}

			if (o.getType().equals(ThingType.BLANK_ASSET)) {
				continue;
			}

			if (ps.contains("owl:imports")) {
				continue;
			}

			if (ss.equalsIgnoreCase("owl:Nothing")) {
				continue;
			}

			if (ss.equalsIgnoreCase("owl:Thing")) {
				continue;
			}

			if (os.equalsIgnoreCase("owl:Thing")) {
				continue;
			}

			if (ps.equalsIgnoreCase("rdf:type") && os.equalsIgnoreCase("owl:Thing")) {
				continue;
			}

			if (ps.equalsIgnoreCase("rdf:type") && os.equalsIgnoreCase("owl:Class")) {
				continue;
			}

			if (ps.equalsIgnoreCase("rdfs:subClassOf") && os.equalsIgnoreCase("owl:Thing")) {
				continue;
			}

			if (s.equals(o)) {

				if (ps.equalsIgnoreCase("owl:sameAs")) {
					continue;
				}

				if (ps.equalsIgnoreCase("rdfs:subClassOf")) {
					continue;
				}

				if (ps.equalsIgnoreCase("owl:equivalentClass")) {
					continue;
				}

				if (ps.equalsIgnoreCase("owl:equivalentProperty")) {
					continue;
				}

				if (ps.equalsIgnoreCase("rdfs:subPropertyOf")) {
					continue;
				}
			}

			if (s.getType().equals(ThingType.PLAIN) || s.getType().isNumeric() || s.getType().isXsdString()
					|| s.getType().isXsdDateTime()) {
				continue;
			}

			facts.insert(f);
		}

		logger.info("Number of facts after filtering: {}", facts.size());
		return facts;
	}

	public void observe(ReteEvent event) {
	}

	public void run() {
		logger.info("Initializing BaseVISor..");
		try {
			this.initialize();
			logger.info("There are {} asserted facts in the knowledge base.", rete.getFactCount());

		} catch (Exception e) {
			logger.error("Failed to initialize BaseVISor.", e);
			return;
		}
		logger.info("Initialization complete. Running inference...");

		try {
			rete.run();
		} catch (BaseException e) {
			logger.error("There was an error while running the inference engine.", e);
			return;
		}
		logger.info("Number of facts after running inference: {}", rete.getFactCount());

		logger.info("Exporting Rete to RDF...");
		String rdfDump = toRDF();

		logger.info("Saving RDF to a file...");
		try {
			FileUtils.writeStringToFile(new File(dumpedFilePath), rdfDump, StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.error("There was an error while dumping into file.", e);
		}

		logger.info("Final RDF facts are in {}", dumpedFilePath);
		logger.info("Done.\n");
	}

	public static void main(String[] args) {
//		String COMP_DIRECTORY = ".." + File.separator + "Ontology" + File.separator + "BVR_Files" + File.separator + "WithProceduralAttach" + File.separator;
		String COMP_DIRECTORY = ".." + File.separator + "Ontology" + File.separator + "BVR_Files" + File.separator;
		String DUMP_PATH = "dumpedfiles" + File.separator;

		//new RunBaseVISor(COMP_DIRECTORY + "Comp_FFT.bvr", DUMP_PATH + "bvrDump_Comp_FFT.rdf").run();
		//new RunBaseVISor(COMP_DIRECTORY + "Comp_Magnitude.bvr", DUMP_PATH + "bvrDump_Comp_Magnitude.rdf").run();
		//new RunBaseVISor(COMP_DIRECTORY + "Comp_Threshold.bvr", DUMP_PATH + "bvrDump_Comp_Threshold.rdf").run();
		new RunBaseVISor(COMP_DIRECTORY + "rule_4_concequence.bvr", DUMP_PATH + "bvrDump_maccdc_4.rdf").run();
	}
}
