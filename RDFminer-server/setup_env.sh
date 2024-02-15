#!/bin/bash
ENV_FILE=/app/.env
echo "APP="$APP > $ENV_FILE
echo "RDFMINER_SERVER_PORT="$RDFMINER_SERVER_PORT >> $ENV_FILE
echo "RDFMINER_SERVER_MONGODB_PORT="$RDFMINER_SERVER_MONGODB_PORT >> $ENV_FILE
echo "RDFMINER_SERVER_MONGODB_CONNECTION="$RDFMINER_SERVER_MONGODB_CONNECTION >> $ENV_FILE
echo "RDFMINER_SERVER_AUTH_KEY="$RDFMINER_SERVER_AUTH_KEY >> $ENV_FILE
echo "RDFMINER_SERVER_MONGODB_DATAPATH="$RDFMINER_SERVER_MONGODB_DATAPATH >> $ENV_FILE
echo "RDFMINER_SERVER_REDIS_PORT="$RDFMINER_SERVER_REDIS_PORT >> $ENV_FILE
echo "RDFMINER_SERVER_WS="$RDFMINER_SERVER_WS >> $ENV_FILE
echo "Done !"