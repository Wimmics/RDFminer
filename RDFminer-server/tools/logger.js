const winston = require("winston");

class Logger {
    /**
     * Return the logger and integrate the filename into it
     * @param {String} filename the filename
     */
    static getLogger(filename) {
        return winston.createLogger({
            level: "info",
            format: winston.format.combine(
                // winston.format.colorize({ message: true }),
                winston.format.timestamp({ format: "YYYY-MM-DD HH:mm:ss" }),
                winston.format.errors({ stack: true }),
                winston.format.printf((info) => {
                    const level = info.level.toUpperCase(); 
                    return `${info.timestamp} - ${level}\t- ${filename}\t- ${info.stack || info.message}`
                })
            ),
            transports: [
                new winston.transports.Console(),
                new winston.transports.File({ filename: "logs/" + new Date().toISOString().slice(0,10) + ".log" }),
            ],
        });
    }
}


module.exports = Logger;