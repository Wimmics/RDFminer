const path = require("path");

// return the swagger.json into folder /doc
function get(req, res) {
    // res.status(200).send(html);
    res.sendFile(path.resolve('swagger-html/index.html'));
}


module.exports = { get }