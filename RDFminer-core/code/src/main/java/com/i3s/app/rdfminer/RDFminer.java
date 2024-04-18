/**
 * 
 */
package com.i3s.app.rdfminer;

import com.i3s.app.rdfminer.generator.Generator;
import com.i3s.app.rdfminer.generator.axiom.RandomAxiomGenerator;
import com.i3s.app.rdfminer.generator.shacl.RandomShapeGenerator;
import com.i3s.app.rdfminer.launcher.Evaluator;
import com.i3s.app.rdfminer.launcher.GrammaticalEvolution;
import com.i3s.app.rdfminer.output.Results;
import com.i3s.app.rdfminer.output.SimilarityMap;
import com.i3s.app.rdfminer.sparql.corese.CoreseEndpoint;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * The main class of the RDFMiner experimental tool.
 * <p>
 * More information about OWL 2 may be found in the
 * <a href="http://www.w3.org/TR/2012/REC-owl2-quick-reference-20121211/">OWL2
 * Quick Reference, 2nd Edition</a>.
 * </p>
 * 
 * @author Andrea G. B. Tettamanzi & Rémi FELIN
 *
 */
public class RDFminer {

	private static final Logger logger = Logger.getLogger(RDFminer.class.getName());

	// Novelty search
	public static SimilarityMap similarityMap = null;

	private final Parameters parameters;

	private Results results;

	public RDFminer(Parameters parameters, Results results) {
		this.parameters = parameters;
		this.results = results;
	}

	/**
	 * The entry point of the RDFMiner application.
	 */
	public void exec() throws InterruptedException, ExecutionException, URISyntaxException, IOException {
		// load additional librairies
		System.loadLibrary(Global.SO_LIBRARY);
		// Configuring logger on a specific file
		configureFileLogger();
		// Print the banner of RDFminer
		logRDFminerBanner();
		//
		Generator generator;
		GrammaticalEvolution evolution;
		Evaluator evaluator;
		// Launching the appropriate feature according to the parameters
		switch (parameters.getMod()) {
			// Shape Mining
			// require SHACL shapes generator to build well-formed candidates
			// grammatical evolution will be used
			case Mod.SHAPE_MINING:
				// count the total number of RDF triples in the graph
				// use the result for each prob. shacl validation
				CoreseEndpoint endpoint = new CoreseEndpoint(this.parameters);
				Global.nTriples = endpoint.count("*", "?s ?p ?o", false);
				// init generator with BNF grammar provided by user
				generator = new RandomShapeGenerator(this.parameters);
				evolution = new GrammaticalEvolution(generator, this.results);
				try {
					evolution.run();
				} catch (Exception e) {
					logger.error("error during the GE process ...");
					logger.error(e.getMessage());
					// System.exit(0);
				}
				break;
			// Axiom Mining
			// require OWL axiom generator to build well-formed candidates
			// grammatical evolution will be used
			case Mod.AXIOM_MINING:
				// init generator with BNF grammar provided by user
				generator = new RandomAxiomGenerator(this.parameters, true);
				evolution = new GrammaticalEvolution(generator, this.results);
				try {
					evolution.run();
				} catch (Exception e) {
					logger.error("error during the GE process ...");
					logger.error(e.getMessage());
					// System.exit(0);
				}
				break;
			// OWL axioms or SHACL shapes assessment
			// The system can assess (1) SubClassOf and DisjointClass oxioms and (2) SHACL shapes
			case Mod.AXIOM_ASSESSMENT:
			case Mod.SHAPE_ASSESSMENT:
				// launch evaluator
				evaluator = new Evaluator(this.parameters, this.results);
				evaluator.run();
				break;
			default:
				logger.error("This mod is not recognized ! provided mod code: " + this.parameters.getMod());
				break;
		}
	}

	private void configureFileLogger() {
		String filePath = Global.LOGS + this.results.getLogs();
		// set properties
		Properties props = new Properties();
		props.put("log4j.rootLogger", "INFO, A1");
		props.put("log4j.appender.A1", "org.apache.log4j.RollingFileAppender");
		props.put("log4j.appender.A1.File", filePath);
		// Deal with 'very' huge experiments
		props.put("log4j.appender.A1.MaxFileSize", "500MB");
		props.put("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
		props.put("log4j.appender.A1.layout.ConversionPattern", "%d [%t] %-5p %c - %m%n");
		// set props for Log4j
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(props);
	}

	private static void logRDFminerBanner() {
		for(String line : Global.BANNER) {
			logger.info(line);
		}
		logger.info("This is RDFminer v." + Global.VERSION + " !");
	}

	public Parameters getUsedParameters() {
		return parameters;
	}
}
