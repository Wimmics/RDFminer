#!/bin/bash
ENV_FILE=/app/.env
echo "APP="$APP > $ENV_FILE
echo "VUE_APP_RDFMINER_SERVER_PORT="$RDFMINER_SERVER_PORT >> $ENV_FILE
echo "Done !"