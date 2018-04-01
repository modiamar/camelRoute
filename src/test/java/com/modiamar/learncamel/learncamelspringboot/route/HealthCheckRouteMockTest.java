package com.modiamar.learncamel.learncamelspringboot.route;

import com.modiamar.learncamel.learncamelspringboot.route.processor.HealthCheckProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.*;

@RunWith(CamelSpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("mock")
public class HealthCheckRouteMockTest extends CamelTestSupport {

    @Autowired
    CamelContext context; // need this for when the application starts

    @Autowired
    FileConfiguration fileConfiguration;

    @Autowired
    Environment environment;

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    protected CamelContext createCamelContext(){
        return context;
    };

    @Autowired
    private HealthCheckProcessor healthCheckProcessor;

//    @Override
//    public RouteBuilder createRouteBuilder(){
//        // When test case runs, it will only launch the HealthCheckRoute;
//        return new HealthCheckRoute();
//    }

    @Before
    public void setUp(){

    }

    @Test
    public void testHealthEndpoint(){
        String input = "{\"status\":\"UP\"}";
//        MockEndpoint mockEndpoint = getMockEndpoint(fileConfiguration.getHealthEndpoint());
//        mockEndpoint.expectedMessageCount(1);
//        mockEndpoint.expectedBodiesReceived(message);

        String response = (String) producerTemplate.requestBodyAndHeader(fileConfiguration.getHealthRoute(), input, "env", environment.getProperty("spring.profiles.active"));
        System.out.println("Response was received: " + response);


    }

}