#!/usr/bin/env bash

C_DIR=$(PWD)
ROOT_FOLDER=${C_DIR}/../../
cd ${ROOT_FOLDER}

./gradlew clean build

cd ${C_DIR}

CONTEXT_DIR=${C_DIR}/../../build/libs

docker build -f ${C_DIR}/Dockerfile -t soapserver:v1 ${CONTEXT_DIR}
docker run -d -p 8080:8080 soapserver:v1
