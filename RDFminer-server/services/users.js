// TODO: s'inspirer du backend effectué dans le projet CV-Angular
// https://github.com/RemiFELIN/cv-angular/blob/master/backend/server.js
const User = require("../model/user");
// logger
const Logger = require("../tools/logger.js");
const path = require("path");
const logger = Logger.getLogger(path.basename(__filename));
const bcrypt = require("bcryptjs");
const jwt = require('jsonwebtoken');

function login(req, res) {
    // let decoded = Auth.verify(req, res);
    // if (decoded != null) {
    User.findOne({ username: req.query.username }).then((user) => {
        if (!user) {
            return res.status(401).send({ auth: false, message: 'Wrong username and/or password' });
        } 
        logger.info("GET /login - verifying password ...");
        // compare password submitted vs expected
        if (bcrypt.compareSync(req.query.password, user.password)) {
            var token = jwt.sign({ id: user._id }, process.env.RDFMINER_SERVER_AUTH_KEY, {
                expiresIn: 86400 // == 24 hours
            });
            logger.info("GET /login - OK - " + token);
            let u = {};
            u.username = user.username;
            u.id = user._id;
            u.token = token;
            res.status(200).send(u);
        } else {
            logger.error("GET /login - the password is not correct ...");
            return res.status(200).send({auth: false, token: null});
        }
    }).catch((error) => {
        logger.error("GET /login - error when fetching user: " + error);
        return res.status(500).send({ auth: false, message: 'Internal error' });
    });
}

function isExists(req, res) {
    // logger.info("check if this user already exists ...");
    User.findOne({ username: req.query.username }).then((user) => {
        if (!user) return res.status(200).send(false);
        else return res.status(200).send(true);
    }).catch((error) => {
        return res.status(500).send(error);
    });
}

function register(req, res) {
    logger.info("POST /register");
    const user = new User();
    user.username = req.body.username;
    user.password = bcrypt.hashSync(req.body.password);
    // save user
    user.save().then((data) => {
        if (data) {
            return res.status(200).send(data);
        } else {
            return res.status(401).send("User cannot be created...");
        }
    }).catch((error) => {
        return res.status(500).send(error);
    });
}

function logout(req, res) {
    return res.status(200).send({ auth: false, token: null });
}

module.exports = { login, isExists, register, logout }