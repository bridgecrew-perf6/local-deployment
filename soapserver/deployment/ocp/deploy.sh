#!/bin/bash

set -e

DEPLOYMENT_ROOT=$(pwd)/../../..

source ${DEPLOYMENT_ROOT}/env.sh
source ${DEPLOYMENT_ROOT}/common/common.sh

check_var SOAPSERVER_NAMESPACE

oc process -f template.yaml \
  -p NAMESPACE=${SOAPSERVER_NAMESPACE} | $(command $1 ${SOAPSERVER_NAMESPACE})
