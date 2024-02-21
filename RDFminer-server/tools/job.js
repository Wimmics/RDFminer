// logger
const Logger = require("../tools/logger.js");
const path = require("path");
const logger = Logger.getLogger(path.basename(__filename));
const Project = require("../model/project");
const Status = require('../model/status');
const socket = require("../servers/express.js");
const request = require("request");

// const ws = path.resolve("/home/rfelin/projects/RDFMining/");
// const rdfminer = "rdfminer";

async function exec(job) {
    return new Promise(async (resolve, reject) => {
        try {
            // logger.info("[" + job.data.project.projectName + "] Updating project status...");
            // logger.info(JSON.stringify(job.data));
            // update status
            Project.updateOne(
                { userID: job.data.userID, projectName: job.data.projectName },
                { status: Status.IN_PROGRESS }
            ).then(() => {
                socket.io.emit("update-status", { projectName: job.data.projectName, status: Status.IN_PROGRESS });
                // POST request of RDFminer-core 
                // /core/start 
                // body (url encoded):   "params": Object (:= project)
                var options = {
                    'method': 'POST',
                    'url': 'http://rdfminer:8891/core/start',
                    'headers': {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    form: {
                        'params': JSON.stringify(job.data)
                    }
                };
                request.post(options, (error, response) => {
                    if (error) {
                        logger.error("/POST " + options.url + " - error when performing the request: " + error);
                        reject(error);
                    } else {
                        // as RDFminer-core server will update the results, we don't need to save the response
                        logger.info("/POST " + options.url + " - code:" + response.statusCode);
                        // project is done
                        Project.updateOne(
                            { userID: job.data.userID, projectName: job.data.projectName },
                            { status: Status.FINISH }
                        ).then(() => {
                            // success !
                            resolve("completed");
                        }).catch((error) => {
                            reject(error);
                        });
                    }
                });
            }).catch((error) => {
                reject(error);
            });
        } catch (error) {
            reject(error);
        }
    });

}

module.exports = { exec }
