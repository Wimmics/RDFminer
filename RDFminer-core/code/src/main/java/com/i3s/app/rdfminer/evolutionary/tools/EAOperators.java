package com.i3s.app.rdfminer.evolutionary.tools;

import com.i3s.app.rdfminer.Parameters;
import com.i3s.app.rdfminer.evolutionary.geva.Individuals.FitnessPackage.BasicFitness;
import com.i3s.app.rdfminer.evolutionary.geva.Individuals.GEIndividual;
import com.i3s.app.rdfminer.evolutionary.geva.Individuals.Individual;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.crossover.SinglePointCrossover;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.crossover.SubtreeCrossover;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.crossover.SwapCrossover;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.crossover.TwoPointCrossover;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.mutation.IntFlipByteMutation;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.mutation.IntFlipMutation;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.mutation.NodalMutation;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.mutation.SubtreeMutation;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.selection.EliteOperationSelection;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.selection.ProportionalRouletteWheel;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.selection.ScaledRouletteWheel;
import com.i3s.app.rdfminer.evolutionary.geva.Operator.selection.TournamentSelect;
import com.i3s.app.rdfminer.evolutionary.geva.Util.Random.MersenneTwisterFast;
import com.i3s.app.rdfminer.evolutionary.types.TypeCrossover;
import com.i3s.app.rdfminer.evolutionary.types.TypeMutation;
import com.i3s.app.rdfminer.evolutionary.types.TypeSelection;

import java.util.ArrayList;

public class EAOperators {

    private Parameters parameters;

    private final int selectionSize;

    private final int tournamentSize;

    private final int eliteSize;

    public EAOperators(Parameters parameters) {
        this.parameters = parameters;
        this.selectionSize = (int) (parameters.getSelectionRate() * parameters.getPopulationSize());
        this.tournamentSize = (int) (parameters.getTournamentSelectionRate() * parameters.getPopulationSize());
        this.eliteSize = (int) (parameters.getEliteSelectionRate() * parameters.getPopulationSize());
    }

    public ArrayList<GEIndividual> getElitesFromPopulation(ArrayList<GEIndividual> population) {
        ArrayList<GEIndividual> elites = new ArrayList<>();
        // find the best individuals
        EliteOperationSelection eos = new EliteOperationSelection(this.eliteSize);
        eos.doOperation(population);
        for(Individual elite : eos.getSelectedPopulation().getAll()) {
            elites.add((GEIndividual) elite);
        }
        return elites;
    }

    /**
     * Find the individuals to select, depending on the selection mod
     * @param population the entire population
     * @return selected population
     */
    public ArrayList<GEIndividual> getSelectionFromPopulation(ArrayList<GEIndividual> population) {
//        Parameters parameters = Parameters.getInstance();
        ArrayList<GEIndividual> selectedPopulation = new ArrayList<>();
        switch(this.parameters.getSelectionType()) {
            default:
            case TypeSelection.PROPORTIONAL_ROULETTE_WHEEL:
                ProportionalRouletteWheel prw = new ProportionalRouletteWheel(this.selectionSize, new MersenneTwisterFast());
                prw.doOperation(population);
                for(Individual selected : prw.getSelectedPopulation().getAll()) {
                    selectedPopulation.add((GEIndividual) selected);
                }
                break;
            case TypeSelection.SCALED_ROULETTE_WHEEL:
                ScaledRouletteWheel srw = new ScaledRouletteWheel(this.selectionSize, new MersenneTwisterFast());
                srw.doOperation(population);
                for(Individual selected : srw.getSelectedPopulation().getAll()) {
                    selectedPopulation.add((GEIndividual) selected);
                }
                break;
            case TypeSelection.TOURNAMENT_SELECT:
                TournamentSelect ts = new TournamentSelect(this.selectionSize, this.tournamentSize, new MersenneTwisterFast());
                ts.doOperation(population);
                for(Individual selected : ts.getSelectedPopulation().getAll()) {
                    selectedPopulation.add((GEIndividual) selected);
                }
                break;
        }
        return selectedPopulation;
    }

    public void crossover(ArrayList<GEIndividual> couple) {
        switch (this.parameters.getCrossoverType()) {
            default:
            case TypeCrossover.SINGLE_POINT:
                // Single-point crossover
                SinglePointCrossover spc = new SinglePointCrossover(this.parameters.getProCrossover(), new MersenneTwisterFast());
                spc.setFixedCrossoverPoint(true);
                spc.doOperation(couple);
                break;
            case TypeCrossover.TWO_POINT:
                // Two point crossover
                TwoPointCrossover tpc = new TwoPointCrossover(this.parameters.getProCrossover(), new MersenneTwisterFast());
                tpc.setFixedCrossoverPoint(true);
                tpc.doOperation(couple);
                break;
            case TypeCrossover.SUBTREE:
                // subtree crossover
                // special implementation due to the original implementation by GEVA developers
                SubtreeCrossover stc = new SubtreeCrossover(this.parameters.getProCrossover(), new MersenneTwisterFast());
                stc.doOperation(couple);
                break;
            case TypeCrossover.SWAP:
                // Swap crossover
                // contribution testing for ShaMPA
                SwapCrossover swp = new SwapCrossover(this.parameters.getProCrossover(), new MersenneTwisterFast());
                swp.doOperation(couple);
                break;
        }
    }

    public void mutation(ArrayList<GEIndividual> couple) {
        switch (this.parameters.getMutationType()) {
            default:
            case TypeMutation.INT_FLIP:
                IntFlipMutation ifm = new IntFlipMutation(this.parameters.getProMutation(), new MersenneTwisterFast());
                ifm.doOperation(couple);
                break;
            case TypeMutation.NODAL:
                NodalMutation nm = new NodalMutation(this.parameters.getProMutation(), new MersenneTwisterFast());
                nm.doOperation(couple);
                break;
            case TypeMutation.SUBTREE:
                SubtreeMutation sm = new SubtreeMutation(this.parameters.getProMutation(), new MersenneTwisterFast());
                sm.doOperation(couple);
                break;
            case TypeMutation.INT_FLIP_BYTE:
                IntFlipByteMutation ifbm = new IntFlipByteMutation(this.parameters.getProMutation(), new MersenneTwisterFast());
                ifbm.doOperation(couple);
                break;
        }
    }

    public static void main(String[] args) {
        ArrayList<GEIndividual> p = new ArrayList<>();
        GEIndividual i1 = new GEIndividual();
        GEIndividual i2 = new GEIndividual();
        GEIndividual i3 = new GEIndividual();
        i1.setFitness(new BasicFitness(2, i1));
        i1.setFitness(new BasicFitness(3, i2));
        i1.setFitness(new BasicFitness(4, i3));
        p.add(i1);
        p.add(i2);
        p.add(i3);
        ProportionalRouletteWheel prw = new ProportionalRouletteWheel();
        prw.doOperation(p);
        for(Individual selected : prw.getSelectedPopulation().getAll()) {
            System.out.println(selected.getFitness());
        }
    }

}
