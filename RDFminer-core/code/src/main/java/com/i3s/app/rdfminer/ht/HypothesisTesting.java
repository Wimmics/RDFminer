package com.i3s.app.rdfminer.ht;

import com.i3s.app.rdfminer.entity.shacl.Shape;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

public class HypothesisTesting {

    public Shape shape;
    private Double X2;
    public boolean isAccepted;

    private double pValue;
    private double alpha;

    public HypothesisTesting(double pValue, double alpha) {
        this.pValue = pValue;
        this.alpha = alpha;
    }

    public void eval(Shape shape) {
        double nExcTheo = shape.referenceCardinality * this.pValue;
        double nConfTheo = shape.referenceCardinality - nExcTheo;
        if(shape.numExceptions <= nExcTheo) {
            // if observed error is lower, accept the shape
            shape.accepted = true;
        } else if (nExcTheo >= 5 && nConfTheo >= 5) {
            // apply statistic test X2
            shape.pValue = (Math.pow(shape.numExceptions - nExcTheo, 2) / nExcTheo) +
                    (Math.pow(shape.numConfirmations - nConfTheo, 2) / nConfTheo);
            // logger.info("Hypothesis testing of " + shape.absoluteIri + ": pVal=" + shape.pValue);
            double critical = new ChiSquaredDistribution(1).inverseCumulativeProbability(1 - this.alpha);
            // test value and accept it if it's lower than critical value
            shape.accepted = shape.pValue <= critical;
        } else {
            // rejected !
            shape.accepted = false;
        }
    }

    public String getAcceptanceTriple() {
        return this.shape.relativeIri + " ex:acceptance \""+ this.isAccepted + "\"^^xsd:boolean .\n";
    }

    public String getX2ValueTriple() {
        return this.shape.relativeIri + " ex:pvalue \"" + this.X2 + "\"^^xsd:double .\n";
    }

    public Double getX2() {
        return X2;
    }

    public double getMaxMassFunction(Shape shape) {
        BinomialDistribution bd = new BinomialDistribution(shape.referenceCardinality, this.pValue);
        return bd.probability((int) Math.floor(bd.getNumericalMean()));
    }

    public static void main(String[] args) {
        System.out.println((int) Math.floor(2.2));
        System.out.println((int) Math.floor(2.9));
        BinomialDistribution bd = new BinomialDistribution(310, 0.5);
        System.out.println(bd.getNumericalMean());
        System.out.println(bd.probability(155));
    }
}
