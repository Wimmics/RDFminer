const Project = require("../model/project");
const Result = require("../model/results");
const Status = require("../model/status");
const queue = require("../servers/queue");
// const Experience = require("../model/experience");
// logger
const Logger = require("../tools/logger.js");
const path = require("path");
const logger = Logger.getLogger(path.basename(__filename));
const auth = require("../auth/auth");
const socket = require("../servers/express");

async function create(req, res) {
    const decoded = auth.verify(req, res);
    if (decoded != null) {
        logger.info("POST /project - " + decoded.id + " - create()");
        const project = new Project();
        const results = new Result();
        results.save().then((data) => {
            if (data) {
                project.resultsID = data._id.toString();
                logger.info("POST /project - result " + project.resultsID + " has been create and binded to this project");
                // socket.io.emit("results-created");
                // set general settings
                const body = req.body;
                // id's
                project.userID = decoded.id;
                project.projectName = body.projectName;
                project.status = Status.PENDING;
                // rest of values
                project.mod = body.mod;
                project.prefixes = body.prefixes;
                project.namedDataGraph = body.namedDataGraph;
                project.grammar = body.grammar;
                project.populationSize = body.populationSize;
                project.stopCriterion = body.stopCriterion;
                project.effort = body.effort;
                project.maxMiningTime = body.maxMiningTime;
                project.sizeChromosome = body.sizeChromosome;
                project.maxWrap = body.maxWrap;
                project.eliteSelectionRate = body.eliteSelectionRate;
                project.tournamentSelectionRate = body.tournamentSelectionRate;
                project.selectionType = body.selectionType;
                project.selectionRate = body.selectionRate;
                project.crossoverType = body.crossoverType;
                project.proCrossover = body.proCrossover;
                project.mutationType = body.mutationType;
                project.proMutation = body.proMutation;
                project.shapes = body.shapes;
                project.axioms = body.axioms;
                project.probShaclP = body.probShaclP;
                project.probShaclAlpha = body.probShaclAlpha;
                project.sparqlTimeOut = body.sparqlTimeOut;
                // push into db
                project.save().then((project) => {
                    if (project) {
                        // adding on queue
                        queue.addJob(project);
                        // job submitted to the queue
                        return res.status(200).send(project);
                    }
                }).catch((error) => {
                    logger.error("POST /project - " + decoded.id + " - the project has not been created ...");
                    res.send(error);
                });
            } else {
                logger.error("POST /project - " + decoded.id + " - the result has not been created ...");
            }
        }).catch((error) => {
            logger.error("POST /project - " + decoded.id + " - " + error);
            return res.status(401).send();
        });
        
    }
}

function deleteProject(req, res) {
    const decoded = auth.verify(req, res);
    if (decoded != null) {
        logger.info("DELETE /project - " + decoded.id + " - delete()");
        Project.deleteOne({ userID: decoded.id, projectName: req.query.projectName }).then((project) => {
            logger.info("DELETE /project - " + decoded.id + " - delete results related to this project (if exists)...");
            Result.deleteOne({ _id: project.resultsID }).then((data) => {
                if (data) {
                    logger.info("DELETE /project - " + decoded.id + " - results (" + data._id + ") deleted !");
                }
            }).catch((error) => {
                logger.error("DELETE /project - " + decoded.id + " - results cannot be deleted: " + error);
                return res.status(401).send(false);
            });
            // socket.io.emit("deleteProject", project);
            // console.log("/project/delete: " + data);
            logger.info("DELETE /project - " + decoded.id + " - project (" + project._id + ") deleted !");
            return res.status(200).send(true);
        }).catch((error) => {
            logger.error("DELETE /project - " + decoded.id + " - project cannot be deleted: " + error);
            return res.status(401).send(false);
        });
    }
}

function getProjectsByUser(req, res) {
    const decoded = auth.verify(req, res);
    if (decoded != null) {
        logger.info("GET /project - " + decoded.id + " - getProjectsByUser()");
        Project.find({ userID: decoded.id }).then((projects) => {
            return res.status(200).send(projects);
        }).catch((error) => {
            logger.error("GET /project - " + decoded.id + " - error when fetching projects: " + error);
            return res.status(401).send(error);
        });
    }
}

function getProject(req, res) {
    const decoded = auth.verify(req, res);
    if (decoded != null) {
        logger.info("GET /project - " + decoded.id + " - getProject()");
        if (req.query.projectName != null) {
            Project.find({ userID: decoded.id, projectName: req.query.projectName }).then((project) => {
                res.status(200).send(project);
                // console.log(project);
            }).catch((error) => {
                logger.error("GET /project - " + decoded.id + " - error when fetching projects: " + error);
                return res.status(401).send(error);
            });
        } else if (req.query.id != null) {
            Project.find({ userID: decoded.id, _id: req.query.id }).then((project) => {
                res.status(200).send(project);
                // console.log(project);
            }).catch((error) => {
                logger.error("GET /project - " + decoded.id + " - error when fetching projects: " + error);
                return res.status(401).send(error);
            });
        } else {
            logger.error("GET /results - " + decoded.id + " - a project ID or the project name must be provided in the query ...")
            return res.status(401).send("a project ID or the project name must be provided in the query ...");
        }
        
    }
}

module.exports = { create, deleteProject, getProjectsByUser, getProject }