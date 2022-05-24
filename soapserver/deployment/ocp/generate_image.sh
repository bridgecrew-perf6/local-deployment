#¡/bin/bash

set -e

DEPLOYMENT_ROOT=$(pwd)/../../..

source ${DEPLOYMENT_ROOT}/env.sh
source ${DEPLOYMENT_ROOT}/common/common.sh

openshift_registry=$(oc -n openshift-image-registry get routes default-route -o jsonpath='{.spec.host}')

docker login $openshift_registry --username oc --password $(oc whoami -t)

docker build -t $openshift_registry/${SOAPSERVER_NAMESPACE}/soapserver:v1 --build-arg JAR_FILE='build/libs/*.jar' -f ../docker/Dockerfile ../..
docker push $openshift_registry/${SOAPSERVER_NAMESPACE}/soapserver:v1
