# The aim of this file is to setup an complete environment to launch RDFMiner tool
# Get maven:3-jdk-11 image from Docker Hub
FROM maven:3-jdk-11
# Import env variables
ARG RDFMINER_VERSION
ARG RDFMINER_SERVER_PORT
ARG CORESE_PORT
ARG HOME
ARG RDFMINER_CORE_PORT
ENV RDFMINER_VERSION=$RDFMINER_VERSION
ENV RDFMINER_SERVER_PORT=$RDFMINER_SERVER_PORT
ENV CORESE_PORT=$CORESE_PORT
ENV HOME=$HOME
ENV RDFMINER_CORE_PORT=$RDFMINER_CORE_PORT
# Update
RUN apt-get update
# Install gcc 
RUN apt install gcc -y
RUN echo "\n"$(gcc --version)"\n"
# copy all project in HOME folder in image to build
COPY . $HOME
# Compile c code by running "compile_c_code" shell code
RUN echo -n "Compile rdfminer_entity_Entity.so ... " && sh $HOME/scripts/compile_c_code.sh && echo "Done."
# run command 'mvn clean install' to build RDFMiner jar 
RUN cd $HOME/code && mvn clean install
# Uninstall GCC and clean
RUN apt-get remove gcc -y
RUN apt autoremove -y
RUN apt-get clean
# chmod 777 run.sh file
RUN chmod +x $HOME/scripts/run.sh
# launch
ENTRYPOINT ["./rdfminer/scripts/run.sh"]
