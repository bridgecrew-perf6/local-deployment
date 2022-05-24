package com.ibm.csm.refarch.ace;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/ws/*");
	}

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
		validatingInterceptor.setValidateRequest(true);
		validatingInterceptor.setValidateResponse(true);
		validatingInterceptor.setXsdSchema(customerDetailsSchemaSchema());
		interceptors.add(validatingInterceptor);
	}

	@Bean(name = "customerDetails")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema customerDetailsSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("CustomerDetailsPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://ibm.com/CustomerDetails");
		wsdl11Definition.setSchema(customerDetailsSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema customerDetailsSchemaSchema() {
		return new SimpleXsdSchema(new ClassPathResource("customer_details.xsd"));
	}
}
