package com.modiamar.learncamel.learncamelspringboot.route.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class HealthCheckProcessor implements org.apache.camel.Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String endpointResponse = exchange.getIn().getBody(String.class);
        log.info("Health check response was {}", endpointResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(endpointResponse, new TypeReference<Map<String, Object>>() {
        });
        log.info("Map read is {}", map);

        StringBuilder errorLog = null;
        for (String key : map.keySet()) {
            if (map.get(key).toString().contains("DOWN")){
                if (errorLog == null){
                    errorLog = new StringBuilder();
                }
                errorLog.append(key + " component in the route is down \n ");
            }
        }
        if (errorLog != null){
            log.info("Exception message is: " + errorLog.toString());

            exchange.getIn().setHeader("error", true);
            exchange.getIn().setBody(errorLog.toString());
            exchange.setProperty(Exchange.EXCEPTION_CAUGHT, errorLog.toString());
        }

    }
}
