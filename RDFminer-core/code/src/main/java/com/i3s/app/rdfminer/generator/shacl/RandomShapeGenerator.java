package com.i3s.app.rdfminer.generator.shacl;

import com.i3s.app.rdfminer.Parameters;
import com.i3s.app.rdfminer.evolutionary.geva.Util.Random.MersenneTwisterFast;
import com.i3s.app.rdfminer.evolutionary.geva.Util.Random.RandomNumberGenerator;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This class is used to generate SHACL Shapes randomly according to this grammar
 * @author Rémi FELIN
 */
public class RandomShapeGenerator extends ShapeGenerator {

    /**
     * The random number generator used to generate axioms.
     */
    protected RandomNumberGenerator random;

    /**
     * Constructs a new SHACL Shape generator for the language described by the given grammar.
     *
     * @throws URISyntaxException Error concerning the syntax of the given URL
     * @throws IOException        Error concerning the execution of the GET request
     */
    public RandomShapeGenerator(Parameters parameters) throws URISyntaxException, IOException {
        super(parameters);
        // Set up a pseudo-random number generator
        random = new MersenneTwisterFast(System.currentTimeMillis());
    }

//    public static void main(String[] args) throws URISyntaxException, IOException {
//        Generator test = new RandomShapeGenerator("/user/rfelin/home/projects/RDFMining/IO/shacl-shapes-test.bnf");
//    }

}
