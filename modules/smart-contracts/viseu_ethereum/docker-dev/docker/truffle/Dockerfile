
FROM ubuntu:18.04
USER root
WORKDIR /home/app
RUN apt-get update
RUN apt-get -y install curl gnupg
RUN curl -sL https://deb.nodesource.com/setup_14.x  | bash -
RUN apt-get -y install nodejs
RUN npm install
RUN npm install -g truffle@5.3.4
