const Result = require("../model/results");
const Project = require("../model/project");
// logger
const Logger = require("../tools/logger.js");
const path = require("path");
const logger = Logger.getLogger(path.basename(__filename));
const auth = require("../auth/auth");
const socket = require("../servers/express");

function get(req, res) {
    const decoded = auth.verify(req, res);
    if (decoded != null) {
        logger.info("GET /results - " + decoded.id + " - get()");
        // find results linked to the project
        if (req.query.resultsID != null) {
            Result.findOne({ _id: req.query.resultsID }).then((result) => {
                // return the result ID
                if (result == null) {
                    logger.error("GET /results - " + decoded.id + " - this result has not been found ...");
                    return res.status(401).send("this result has not been found ...");
                }
                return res.status(200).send(result);
            });
        } else if (req.query.projectID != null) {
            Project.findOne({ userID: decoded.id, projectName: req.query.projectName }).then((project) => {
                if (project != null) {
                    Result.findOne({ _id: project.resultsID }).then((result) => {
                        // return the result ID
                        if (result == null) {
                            logger.error("GET /results - " + decoded.id + " - this result has not been found ...");
                            return res.status(401).send("this result has not been found ...");
                        }
                        return res.status(200).send(result);
                    });
                } else { 
                    logger.error("GET /results - " + decoded.id + " - this project has not been found ...")
                    return res.status(401).send("this project has not been found ...");
                }
            });
        } else {
            logger.error("GET /results - " + decoded.id + " - a project ID must be provided in the query ...")
            return res.status(401).send("a project ID must be provided in the query ...");
        }
    }
}

function update(req, res) {
    logger.info("PUT /results - " + req.body._id + " - update()");
    // update this results 
    Result.findOneAndUpdate({ _id: req.body._id }, req.body, { new: true }).then((result) => {
        socket.io.emit("update-results", result._id);
        return res.status(200).send(result);
    }).catch((error) => {
        logger.error("PUT /results - " + req.query._id + " - " + error);
        return res.status(401).send("this result has not been found ...");
    });
}

module.exports = { get, update }