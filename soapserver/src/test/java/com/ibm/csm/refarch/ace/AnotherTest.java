package com.ibm.csm.refarch.ace;

import com.ibm.customerdetails.CustomerDetails;
import com.ibm.customerdetails.CustomerDetailsRequest;
import com.ibm.customerdetails.CustomerDetailsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnotherTest {

    private final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    @LocalServerPort
    private int port = 0;

    @BeforeEach
    public void init() throws Exception {
        marshaller.setPackagesToScan(ClassUtils.getPackageName(CustomerDetailsRequest.class));
        marshaller.afterPropertiesSet();
    }

    @Test
    public void testSendAndReceive() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        CustomerDetailsRequest request = createSampleCustomerDetailsRequest("IBM", "555-5555555");
        CustomerDetailsResponse response =
                (CustomerDetailsResponse) ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request);


        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getCustomerId()).isNotBlank();
    }

    @Test
    public void testSendAndReceive_givenMissingBrandName() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        CustomerDetailsRequest request = createSampleCustomerDetailsRequest("", "555-5555555");

        assertThatThrownBy(() -> ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request))
                .hasMessageContaining("Validation error");
    }

    @Test
    public void testSendAndReceive_givenWrongPhoneNumberFormat() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        CustomerDetailsRequest request = createSampleCustomerDetailsRequest("IBM", "555");

        assertThatThrownBy(() -> ws.marshalSendAndReceive("http://localhost:" + port + "/ws", request))
                .hasMessageContaining("Validation error");
    }

    private CustomerDetailsRequest createSampleCustomerDetailsRequest(String brandName, String phoneNumber) {
        CustomerDetailsRequest request = new CustomerDetailsRequest();

        CustomerDetails customerDetails = new CustomerDetails();

        CustomerDetails.ServiceHeader serviceHeader = new CustomerDetails.ServiceHeader();
        serviceHeader.setBrand(brandName);
        customerDetails.setServiceHeader(serviceHeader);


        CustomerDetails.PersonalDetails personalDetails = new CustomerDetails.PersonalDetails();

        CustomerDetails.PersonalDetails.NameDetails nameDetails = new CustomerDetails.PersonalDetails.NameDetails();
        nameDetails.setFirstName("John");
        nameDetails.setLastName("Doe");

        personalDetails.setNameDetails(nameDetails);

        CustomerDetails.PersonalDetails.ContactDetails contactDetails =
                new CustomerDetails.PersonalDetails.ContactDetails();
        contactDetails.setAddress("123 Main Street");
        contactDetails.setPhone(phoneNumber);

        personalDetails.setContactDetails(contactDetails);

        customerDetails.setPersonalDetails(personalDetails);

        request.setCustomerDetails(customerDetails);
        return request;
    }
}
