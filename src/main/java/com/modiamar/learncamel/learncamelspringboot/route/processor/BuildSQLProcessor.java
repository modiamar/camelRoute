package com.modiamar.learncamel.learncamelspringboot.route.processor;

import com.modiamar.learncamel.learncamelspringboot.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BuildSQLProcessor implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // This will give use access to the exchange record
        Item item = (Item) exchange.getIn().getBody();
        log.info("Item in processor is :" + item);

        StringBuilder query = new StringBuilder();
        switch (item.getType()) {
            case "ADD" :
                query.append("INSERT INTO ITEMS (SKU_NUMBER, ITEM_DESCRIPTION, PRICE) VALUES ('" );
                query.append(item.getSkuNumber() + "', '");
                query.append(item.getItemDescription() + "', ");
                query.append(item.getPrice() + ")");
                break;
            case "UPDATE":
                query.append("UPDATE ITEMS SET PRICE = '");
                query.append(item.getPrice() + "'");
                query.append("WHERE SKU_NUMBER = '" + item.getSkuNumber() + "'");
                break;
            case "DELETE":
                query.append("DELETE FROM ITEMS WHERE SKU_NUMBER = '");
                query.append(item.getSkuNumber() + "'");
                break;
        }
        log.info("Statement is {}", query);

        // Sends the sql statement to the jdbc:dataSource and Camel INTERNALLY processes the SQL execution.
        exchange.getIn().setBody(query.toString());

    }
}
