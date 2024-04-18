/**
 * 
 */
package com.i3s.app.rdfminer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A container of settings
 * This is a singleton !
 * @author Andrea G. B. Tettamanzi & Rémi Felin
 */
@JsonIgnoreProperties(value = { "_id", "__v", "status"})
public class Parameters {

//	private static Parameters instance = null;

	private Parameters() {}

	@JsonProperty("userID")
	private String userID = "admin";

	@JsonProperty("projectName")
	private String projectName = "results";

	@JsonProperty("mod")
	private int mod = 3;

	@JsonProperty("prefixes")
	private String prefixes = Global.PREFIXES;

	@JsonProperty("namedDataGraph")
	private String namedDataGraph;

	@JsonProperty("grammar")
	private String grammar;

	@JsonProperty("populationSize")
	private int populationSize = 100;

	@JsonProperty("stopCriterion")
	private int stopCriterion = 1;

	@JsonProperty("effort")
	private int effort = 5000;

	@JsonProperty("maxMiningTime")
	private int maxMiningTime = 30;

	@JsonProperty("sizeChromosome")
	private int sizeChromosome = 20;

	@JsonProperty("maxWrap")
	private int maxWrap = 1000;

	// Selection

	@JsonProperty("eliteSelectionRate")
	private double eliteSelectionRate = 0.2;

	@JsonProperty("tournamentSelectionRate")
	private double tournamentSelectionRate = 0.1;

	@JsonProperty("selectionType")
	private int selectionType = 3;

	@JsonProperty("selectionRate")
	private double selectionRate = 0.5;

	// Operators

	@JsonProperty("crossoverType")
	private int crossoverType = 1;

	@JsonProperty("proCrossover")
	private double proCrossover = 0.75;

	@JsonProperty("mutationType")
	private int mutationType = 1;

	@JsonProperty("proMutation")
	private double proMutation = 0.01;

	// Shapes or axioms to evaluate

	@JsonProperty("shapes")
	private String shapes;

	@JsonProperty("axioms")
	private String axioms;

	// Probabilistic SHACL

	@JsonProperty("probShaclP")
	private double probShaclP = 0.05;

	@JsonProperty("probShaclAlpha")
	private double probShaclAlpha = 0.05;

	@JsonProperty("resultsID")
	private String resultsID;

	/**
	 * The timeout used for SPARQL queries <i>(default value: 30,000 ms)</i>
	 */
	@JsonProperty("sparqlTimeOut")
	private long sparqlTimeOut = 30000;

	// @TODO: map it on json input when the implementation is finished
	private boolean useNoveltySearch = false;

	public long timeCap = 0;

	public boolean grammaticalEvolution;

	public int maxValCodon = Integer.MAX_VALUE;

	public String getUserID() {
		return userID;
	}

	public String getProjectName() {
		return projectName;
	}

	public int getMod() {
		return mod;
	}

	public String getPrefixes() {
		return prefixes;
	}

	public String getNamedDataGraph() {
		return namedDataGraph;
	}

	public String getGrammar() {
		return grammar;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public int getStopCriterion() {
		return stopCriterion;
	}

	public int getEffort() {
		return effort;
	}

	public int getMaxMiningTime() {
		return maxMiningTime;
	}

	public int getSizeChromosome() {
		return sizeChromosome;
	}

	public int getMaxWrap() {
		return maxWrap;
	}

	public double getEliteSelectionRate() {
		return eliteSelectionRate;
	}

	public double getTournamentSelectionRate() {
		return tournamentSelectionRate;
	}

	public int getSelectionType() {
		return selectionType;
	}

	public double getSelectionRate() {
		return selectionRate;
	}

	public int getCrossoverType() {
		return crossoverType;
	}

	public double getProCrossover() {
		return proCrossover;
	}

	public int getMutationType() {
		return mutationType;
	}

	public double getProMutation() {
		return proMutation;
	}

	public String getShapes() {
		return shapes;
	}

	public String getAxioms() {
		return axioms;
	}

	public double getProbShaclP() {
		return probShaclP;
	}

	public double getProbShaclAlpha() {
		return probShaclAlpha;
	}

	public long getSparqlTimeOut() {
		return sparqlTimeOut;
	}

	public String getResultsID() { return resultsID; }

	public boolean isUseNoveltySearch() {
		return useNoveltySearch;
	}

//	@JsonCreator
//	public static synchronized Parameters getInstance() {
//		if(instance == null) {
//			instance = new Parameters();
//		}
//		return instance;
//	}

}
