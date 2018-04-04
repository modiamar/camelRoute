package com.modiamar.learncamel.learncamelspringboot.route;

import com.modiamar.learncamel.learncamelspringboot.domain.KafkaItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
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

@RunWith(CamelSpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("mock")
@Slf4j
public class KafkaRouteMockTest extends CamelTestSupport{



    @Autowired
    private CamelContext context;

    @Autowired
    protected CamelContext createCamelContext() {
        return context;
    }

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private ConsumerTemplate consumerTemplate;

    @Override
    protected RouteBuilder createRouteBuilder(){
        return new KafkaSimpleCamelRoute();
    }

    @Autowired
    Environment environment;

    @Before
    public void setUp(){

    }

    @Test
    public void unMarshallTest(){
        String item = "{\"transactionType\":\"ADD\", \"sku\":\"100\", \"itemDescription\":\"SamsungTV\", \"price\":\"500.00\"}";
        KafkaItem kafkaItem = (KafkaItem) producerTemplate
                                    .requestBodyAndHeader(environment.getProperty("fromRoute"), item, "env", "mock");
        log.info("Kafka Message unmarshalled is POJO of: {}", kafkaItem);
        assertEquals("100", kafkaItem.getSku());
    }

    @Test
    public void unMarshallTest_error(){
        String item = "{\"transactionType\":\"ADD\", \"sku\":\"\", \"itemDescription\":\"SamsungTV\", \"price\":\"500.00\"}";
        KafkaItem kafkaItem = (KafkaItem) producerTemplate
                .requestBodyAndHeader(environment.getProperty("fromRoute"), item, "env", "mock");
        log.info("Kafka Message unmarshalled is POJO of: {}", kafkaItem);
    }

}
