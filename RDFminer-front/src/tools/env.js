import io from "socket.io-client";

const serverUrl = process.env.NODE_ENV == "production" ? "http://134.59.130.136" : "http://localhost:8890"; // + process.env.VUE_APP_RDFMINER_SERVER_PORT;

export const base = serverUrl;
export const socket = io(serverUrl);