FROM redhat/ubi8

USER root
RUN yum install -y --nodocs openssl make \
  && yum clean all

COPY src /src

ENTRYPOINT []
