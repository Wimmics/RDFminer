#!/bin/bash
if [ "$RDFMINER_FRONT_MOD" == "production" ]; 
then
    npm run serve:prod
else
    npm run serve
fi