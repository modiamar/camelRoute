package com.modiamar.learncamel.learncamelspringboot.route.processor;

import com.modiamar.learncamel.learncamelspringboot.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SuccessProcessor implements org.apache.camel.Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Route completed successfully!");
        exchange.getIn().setBody("Data Updated Successfully");
    }
}
