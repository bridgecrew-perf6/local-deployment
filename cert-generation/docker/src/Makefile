.PHONY: all clean

all: ${PREFIX}-ca ${PREFIX}-ace-server ${PREFIX}-client ${PREFIX}-soap-server

${PREFIX}-ca: ORGANISATION_UNIT = ${ORGANISATION}_CA
${PREFIX}-ca: ${PREFIX}-ca.key ${PREFIX}-ca.crt ${PREFIX}-ca.jks

${PREFIX}-ace-server: ORGANISATION_UNIT = ${ORGANISATION}_ACE
${PREFIX}-ace-server: ${PREFIX}-ace-server.key ${PREFIX}-ace-server.crt

${PREFIX}-client: ORGANISATION_UNIT = ${ORGANISATION}_CLIENT
${PREFIX}-client: ${PREFIX}-client.key ${PREFIX}-client.crt

${PREFIX}-soap-server: ORGANISATION_UNIT = ${ORGANISATION}_SOAP
${PREFIX}-soap-server: ${PREFIX}-soap-server.jks

clean:
	rm -f ${PREFIX}-soap-server.jks
	rm -f ${PREFIX}-ace-server.key ${PREFIX}-ace-server.crt
	rm -f ${PREFIX}-client.key ${PREFIX}-client.crt
	rm -f ${PREFIX}-ca.key ${PREFIX}-ca.crt ${PREFIX}-ca.jks ${PREFIX}-ca.srl

${PREFIX}-ca.key:
	openssl genrsa \
		-des3 \
		-passout pass:passw0rd \
		-out ${PREFIX}-ca.key 4096

${PREFIX}-ca.crt: ${PREFIX}-ca.key
	openssl req \
		-x509 \
		-new \
		-sha256 \
		-key ${PREFIX}-ca.key \
		-passin pass:passw0rd \
		-days 365 \
		-subj "/C=${COUNTRY}/ST=${STATE}/L=${LOCALITY}/O=${ORGANISATION}/OU=${ORGANISATION_UNIT}/CN=${COMMON_NAME}" \
		-out ${PREFIX}-ca.crt

${PREFIX}-ca.jks: ${PREFIX}-ca.crt
	rm -f ${PREFIX}-ca.jks
	keytool -keystore ${PREFIX}-ca.jks \
		-deststorepass passw0rd \
		-storetype jks \
		-importcert \
		-file ${PREFIX}-ca.crt \
		-alias ca-certificate \
		-noprompt

%.key:
	openssl genrsa -out $@ 2048

%.csr: %.key
	openssl req \
		-new \
		-sha256 \
		-key $< \
		-subj "/C=${COUNTRY}/ST=${STATE}/L=${LOCALITY}/O=${ORGANISATION}/OU=${ORGANISATION_UNIT}/CN=${COMMON_NAME}" \
		-sha256 \
		-out $@

%.crt: %.csr ${PREFIX}-ca.crt ${PREFIX}-ca.key
	openssl x509 \
		-req \
		-sha256 \
		-in $< \
		-extfile /src/san.ext \
		-CA ${PREFIX}-ca.crt \
		-CAkey ${PREFIX}-ca.key \
		-CAcreateserial \
		-passin pass:passw0rd \
		-days 365 \
		-sha256 \
		-out $@

%.p12: %.crt %.key
	openssl pkcs12 -export \
		-in $(basename $@).crt \
		-inkey $(basename $@).key \
		-out $@ \
		-passout pass:passw0rd \
		-name $(basename $@)-pkcs12

%.jks: %.p12
	rm -f $@
	keytool -importkeystore \
		-deststorepass passw0rd \
		-destkeypass passw0rd \
		-destkeystore $@ \
		-srckeystore $(basename $@).p12 \
		-srcstoretype PKCS12 \
		-srcstorepass passw0rd \
		-alias $(basename $@)-pkcs12
