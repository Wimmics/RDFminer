const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const ResultsSchema = Schema({
    logs:           String,
    entities:       Array,
    generations:    Array
});

module.exports = mongoose.model('results', ResultsSchema);