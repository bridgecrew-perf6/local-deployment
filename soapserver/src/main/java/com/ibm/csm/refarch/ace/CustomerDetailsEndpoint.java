package com.ibm.csm.refarch.ace;

import com.ibm.customerdetails.CustomerDetailsRequest;
import com.ibm.customerdetails.CustomerDetailsResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.UUID;

@Endpoint
public class CustomerDetailsEndpoint {
	private static final String NAMESPACE_URI = "http://ibm.com/CustomerDetails/";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "customerDetailsRequest")
	@ResponsePayload
	public CustomerDetailsResponse createCustomerDetails(@RequestPayload CustomerDetailsRequest request) {
		CustomerDetailsResponse customerDetailsResponse = new CustomerDetailsResponse();
		customerDetailsResponse.setStatus("success");
		customerDetailsResponse.setCustomerId(UUID.randomUUID().toString());
		return customerDetailsResponse;
	}
}
