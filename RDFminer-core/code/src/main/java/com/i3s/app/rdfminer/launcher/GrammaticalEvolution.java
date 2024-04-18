package com.i3s.app.rdfminer.launcher;

import com.i3s.app.rdfminer.Global;
import com.i3s.app.rdfminer.Parameters;
import com.i3s.app.rdfminer.entity.Entity;
import com.i3s.app.rdfminer.evolutionary.EntityMining;
import com.i3s.app.rdfminer.evolutionary.fitness.Fitness;
import com.i3s.app.rdfminer.evolutionary.geva.Individuals.GEIndividual;
import com.i3s.app.rdfminer.evolutionary.individual.CandidatePopulation;
import com.i3s.app.rdfminer.evolutionary.stopCriterion.ClockWorldStop;
import com.i3s.app.rdfminer.evolutionary.stopCriterion.EffortStop;
import com.i3s.app.rdfminer.evolutionary.stopCriterion.StopCriterion;
import com.i3s.app.rdfminer.evolutionary.types.TypeCrossover;
import com.i3s.app.rdfminer.evolutionary.types.TypeMutation;
import com.i3s.app.rdfminer.evolutionary.types.TypeSelection;
import com.i3s.app.rdfminer.generator.Generator;
import com.i3s.app.rdfminer.output.Results;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class GrammaticalEvolution {

    private static final Logger logger = Logger.getLogger(GrammaticalEvolution.class.getName());

    public static int nRecombinaison;

    public static int nCrossover;

    public static int nMutation;

    private final Parameters parameters;

    private final Generator generator;

    private Results results;

    public GrammaticalEvolution(Generator generator, Results results) {
        this.generator = generator;
        this.parameters = this.generator.getParameters();
        this.results = results;
    }

    /**
     * Implementation of GE
     */
    public void run() {
        // log settings
        logUsedParameters(this.parameters);
        // Generate candidate population
        logger.info("Initializing candidate population ...");
        // init population as GEIndividuals
        ArrayList<GEIndividual> candidatePopulation = new CandidatePopulation(this.generator).initialize();
        // Mapping GEIndividuals as entities
        ArrayList<Entity> entities = Fitness.initializePopulation(candidatePopulation, this.generator);
        // Stop Criterion
        StopCriterion stopCriterion;
        // select the way to stop GE
        switch (this.parameters.getStopCriterion()) {
            default:
            case 1:
                // We stop the mining process based on the maximum time provided
                stopCriterion = new ClockWorldStop(this.parameters.getMaxMiningTime());
                break;
            case 2:
                // Effort determines directly the number of iterations of GE
                stopCriterion = new EffortStop(this.parameters.getPopulationSize(), this.parameters.getEffort());
                break;
        }
        // starting stop criterion (useful for clock-world stop option)
        stopCriterion.start();
        // start GE
        EntityMining mining = new EntityMining(this.parameters, this.results);
        while (!stopCriterion.isFinish()) {
            logger.info("===============");
            logger.info("Generation: " + stopCriterion.getCurGeneration());
            // running an iteration ...
            entities = mining.iterate(this.generator, entities, stopCriterion.getCurGeneration());
            // it means that the mining has been interrupted (exception or by calling /stop web service)
            // finishing GE and return current results
            if (entities == null) {
                break;
            }
            // reset crossover and mutation counter
            nCrossover = 0;
            nMutation = 0;
            // update
            stopCriterion.update();
        }
        logger.info("===============");
        logger.info("Evolutionary process is done...");
    }

    private void logUsedParameters(Parameters parameters) {
        logger.info("=== RESOURCES SETTINGS ==============================");
        logger.info("SPARQL endpoint: " + Global.SPARQL_ENDPOINT);
        logger.info("named data graph: " + parameters.getNamedDataGraph());
        switch (parameters.getStopCriterion()) {
            case 1:
                logger.info("max. mining time: " + parameters.getMaxMiningTime() + " min.");
                break;
            case 2:
                logger.info("effort: " + parameters.getEffort());
                break;
        }
        logger.info("n. of thread(s) used: " + Global.NB_THREADS);
        //
        logger.info("=== POPULATION SETTINGS ==============================");
        logger.info("population size: " + parameters.getPopulationSize());
        logger.info("chromosomes length: " + parameters.getSizeChromosome());
        logger.info("max. wrapping: " + parameters.getMaxWrap());
        //
        logger.info("=== SELECTION SETTINGS ==============================");
        logger.info("elite rate: " + parameters.getEliteSelectionRate());
        logger.info("type selection (recombination): " + TypeSelection.getLabel(parameters.getSelectionType()));
        logger.info("selection rate: " + parameters.getSelectionRate());
        if (parameters.getSelectionType() == TypeSelection.TOURNAMENT_SELECT) {
            logger.info("tournament selection rate " + parameters.getTournamentSelectionRate());
        }
        //
        logger.info("=== OPERATORS SETTINGS ==============================");
        logger.info("type crossover: " + TypeCrossover.getLabel(parameters.getCrossoverType()));
        logger.info("prob. crossover: " + parameters.getProCrossover());
        logger.info("type mutation: " + TypeMutation.getLabel(parameters.getMutationType()));
        logger.info("prob. mutation: " + parameters.getProMutation());
        logger.info("=====================================================");
    }

}
