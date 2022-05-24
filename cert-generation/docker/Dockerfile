FROM redhat/ubi8

USER root
RUN yum install -y --nodocs openssl make java-1.8.0-openjdk-headless \
  && yum clean all

COPY src /src

ENTRYPOINT []
