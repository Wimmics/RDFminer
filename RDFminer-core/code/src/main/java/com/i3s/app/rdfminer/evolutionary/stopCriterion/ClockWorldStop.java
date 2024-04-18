package com.i3s.app.rdfminer.evolutionary.stopCriterion;

public class ClockWorldStop implements StopCriterion {

    private long time;
    private long chrono;
    private int curGeneration;

    private final int maxTimeMining;

    public ClockWorldStop(int maxTimeMining) {
        this.maxTimeMining = maxTimeMining;
    }

    @Override
    public boolean isFinish() {
        if (this.chrono > this.time) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        // start chrono
        this.chrono = System.currentTimeMillis();
        // set current generation
        this.curGeneration = 1;
        // Max time to spent for GE
        // convert time: min to ms
        this.time = chrono + this.maxTimeMining * 60000L;
    }

    @Override
    public void update() {
        // update chrono
        this.chrono = System.currentTimeMillis();
        // update current generation
        this.curGeneration++;
    }

    @Override
    public int getCurGeneration() {
        return this.curGeneration;
    }

}
