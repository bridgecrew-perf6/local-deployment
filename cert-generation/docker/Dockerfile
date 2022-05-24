FROM ibmcom/mq:9.2.0.0-r1-amd64

USER root
RUN microdnf install --nodocs java-1.8.0-openjdk-headless openssl make \
  && microdnf clean all

COPY src /src

ENTRYPOINT []
