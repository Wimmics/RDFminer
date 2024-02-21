const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const ParamsSchema = Schema({
    projectName:                Object,
    mod:                        Object,
    prefixes:                   Object,
    namedDataGraph:             Object,
    grammar:                    Object,
    populationSize:             Object,
    stopCriterion:              Object,
    effort:                     Object,
    maxMiningTime:              Object,
    sizeChromosome:             Object,
    maxWrap:                    Object,
    eliteSelectionRate:         Object,
    tournamentSelectionRate:    Object,
    selectionType:              Object,
    selectionRate:              Object,
    crossoverType:              Object,
    proCrossover:               Object,
    mutationType:               Object,
    proMutation:                Object,
    shapes:                     Object,
    axioms:                     Object,
    probShaclP:                 Object,
    probShaclAlpha:             Object,
    sparqlTimeOut:              Object,
});

module.exports = mongoose.model('params', ParamsSchema);