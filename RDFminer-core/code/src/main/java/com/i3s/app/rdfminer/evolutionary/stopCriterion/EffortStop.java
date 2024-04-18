package com.i3s.app.rdfminer.evolutionary.stopCriterion;

public class EffortStop implements StopCriterion {

    private int curGeneration;

    private final int popSize;

    private final int effort;

    public EffortStop(int popSize, int effort) {
        this.popSize = popSize;
        this.effort = effort;
    }

    @Override
    public boolean isFinish() {
        return this.popSize * this.curGeneration >= this.effort;
    }

    @Override
    public void start() {
        // set current generation
        this.curGeneration = 1;
    }

    @Override
    public void update() {
        // update current generation
        this.curGeneration++;
    }

    @Override
    public int getCurGeneration() {
        return this.curGeneration;
    }
}
