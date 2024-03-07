FROM openjdk:11.0.7-jdk-buster
# Import env variables
ARG CORESE_VERSION
ARG CORESE_PORT
ARG CORESE_STORAGE
ENV CORESE_VERSION=$CORESE_VERSION
ENV CORESE_PORT=$CORESE_PORT
ENV CORESE_STORAGE=$CORESE_STORAGE
ENV CORESE="/usr/local/corese"
# Updating image
RUN apt-get update -y
RUN apt-get install -y wget

RUN mkdir -p $CORESE
# data storage
RUN mkdir -p $CORESE/data
# workspace
WORKDIR $CORESE

# RUN wget http://files.inria.fr/corese/distrib/corese-server-4.5.0.jar
COPY corese-server-$CORESE_VERSION.jar $CORESE/corese-server-$CORESE_VERSION.jar
COPY log4j2.xml $CORESE/log4j2.xml
COPY corese-default-profile.ttl $CORESE/corese-default-profile.ttl
COPY corese-default-properties.properties $CORESE/corese-default-properties.properties
# Inject data into corese
COPY data/ $CORESE/data

ENV CORESESH=$CORESE/corese-server.sh
COPY corese-server.sh $CORESESH
RUN chmod 755 $CORESESH

CMD $CORESESH

