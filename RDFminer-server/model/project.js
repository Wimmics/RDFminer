const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const ProjectSchema = Schema({
    projectName:                String,
    // id's
    userID:                     String,
    resultsID:                  String,
    // to do ; in progress and done
    status:                     Number,
    // values of params
    mod:                        Number,
    prefixes:                   String,
    namedDataGraph:             String,
    grammar:                    String,
    populationSize:             Number,
    stopCriterion:              Number,
    effort:                     Number,
    maxMiningTime:              Number,
    sizeChromosome:             Number,
    maxWrap:                    Number,
    eliteSelectionRate:         Number,
    tournamentSelectionRate:    Number,
    selectionType:              Number,
    selectionRate:              Number,
    crossoverType:              Number,
    proCrossover:               Number,
    mutationType:               Number,
    proMutation:                Number,
    shapes:                     String,
    axioms:                     String,
    probShaclP:                 Number,
    probShaclAlpha:             Number,
    sparqlTimeOut:              Number,
});

module.exports = mongoose.model('projects', ProjectSchema);