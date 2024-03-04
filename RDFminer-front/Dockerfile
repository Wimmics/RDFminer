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
COPY v2/package.json $APP/
RUN npm install
# create a .env file for NodeJS
COPY setup_env.sh $APP/
RUN chmod +x $APP/setup_env.sh
ENV ENVIRONMENT_FOR_NODE=$APP/.env
RUN ./setup_env.sh
# copy all files in app folder
COPY    run.sh             $APP/
COPY    v2/babel.config.js    $APP/
COPY    v2/jsconfig.json      $APP/
COPY    v2/vue.config.js      $APP/
COPY    v2/.eslintrc.js      $APP/
COPY    v2/.eslintignore      $APP/
COPY    v2/src      $APP/src
COPY    v2/public      $APP/public
# launcher
ENTRYPOINT ["./run.sh"]