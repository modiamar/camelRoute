package com.modiamar.learncamel.learncamelspringboot.route;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@RunWith(CamelSpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("mock")
public class SimpleCamelRouteMockTest extends CamelTestSupport {

    @Autowired
    CamelContext context; // need this for when the application starts

    @Autowired
    FileConfiguration fileConfiguration;

    @Autowired
    Environment environment;

    @Autowired
    protected CamelContext createCamelContext(){
        return context;
    };

    @Autowired
    ProducerTemplate producerTemplate;

    @Test
    public void testMockFileMove() throws InterruptedException {
        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Flapper Valve,66\n" +
                "ADD,50,Socks,2";
        String fileName = "fileTest.txt";
        MockEndpoint mockEndpoint = getMockEndpoint(fileConfiguration.getOutputFile());
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(message);

        producerTemplate.sendBodyAndHeader(fileConfiguration.getTimerTime(),message , "env", environment.getProperty("spring.profiles.active"));
        assertMockEndpointsSatisfied();
    }


    @Test
    public void testMockFileMoveAndDB() throws InterruptedException {
        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Flapper Valve,66\n" +
                "ADD,50,Socks,2";
        String successMessage = "Data Updated Successfully";

        MockEndpoint mockEndpoint = getMockEndpoint(fileConfiguration.getOutputFile());
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(message);

        MockEndpoint successMockEndpoint = getMockEndpoint(fileConfiguration.getSuccessRoute()); // What is the endpoint
        successMockEndpoint.expectedMessageCount(1); // How times will it hit
        successMockEndpoint.expectedBodiesReceived(successMessage); // Assert what the expectedValue is

        producerTemplate.sendBodyAndHeader(fileConfiguration.getTimerTime(),message , "env", environment.getProperty("spring.profiles.active"));
        assertMockEndpointsSatisfied();
    }
}