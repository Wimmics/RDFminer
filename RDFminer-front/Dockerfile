FROM node:18

# Import env variables
ARG RDFMINER_SERVER_PORT
ARG RDFMINER_FRONT_MOD
ENV RDFMINER_SERVER_PORT=$RDFMINER_SERVER_PORT
ENV RDFMINER_FRONT_MOD=$RDFMINER_FRONT_MOD
# build app...
ENV APP=/app
RUN mkdir $APP
WORKDIR $APP
# installing dependencies
COPY package.json $APP/
RUN npm install
# create a .env file for NodeJS
COPY setup_env.sh $APP/
RUN chmod +x $APP/setup_env.sh
ENV ENVIRONMENT_FOR_NODE=$APP/.env
RUN ./setup_env.sh
# copy all files in app folder
COPY    public             $APP/public/
COPY    babel.config.js    $APP/
COPY    jsconfig.json      $APP/
COPY    run.sh             $APP/
COPY    ./src/App.vue      $APP/src/
COPY    ./src/main.js      $APP/src/
COPY    ./src/components   $APP/src/components
COPY    ./src/tools        $APP/src/tools
COPY    ./src/vues         $APP/src/vues
# launcher
ENTRYPOINT ["./run.sh"]