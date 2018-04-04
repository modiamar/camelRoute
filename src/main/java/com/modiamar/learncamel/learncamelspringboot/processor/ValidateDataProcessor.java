package com.modiamar.learncamel.learncamelspringboot.processor;

import com.modiamar.learncamel.learncamelspringboot.domain.KafkaItem;
import com.modiamar.learncamel.learncamelspringboot.route.exceptions.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class ValidateDataProcessor implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        KafkaItem kafkaItem = (KafkaItem) exchange.getIn().getBody();
        log.info("Kafka item provided to the ValidationProcessor is: {}", kafkaItem);
        if(ObjectUtils.isEmpty(kafkaItem.getSku())){
            throw new DataException("Sku is null for " + kafkaItem.getItemDescription());
        }
    }

}
