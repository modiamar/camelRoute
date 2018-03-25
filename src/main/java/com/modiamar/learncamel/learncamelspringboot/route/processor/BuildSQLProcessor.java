package com.modiamar.learncamel.learncamelspringboot.route.processor;

import com.modiamar.learncamel.learncamelspringboot.domain.Item;
import com.modiamar.learncamel.learncamelspringboot.route.exceptions.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class BuildSQLProcessor implements org.apache.camel.Processor {

    private static final String table = "ITEMS";
    @Override
    public void process(Exchange exchange) throws Exception {
        // This will give use access to the exchange record
        Item item = (Item) exchange.getIn().getBody();
        log.info("Item in processor is :" + item);

        if(ObjectUtils.isEmpty(item.getSkuNumber())){
            throw new DataException("Sku is null for " + item.getItemDescription());
        }

        StringBuilder query = new StringBuilder();
        switch (item.getType()) {
            case "ADD" :
                query.append("INSERT INTO " + table + " (SKU_NUMBER, ITEM_DESCRIPTION, PRICE) VALUES ('" );
                query.append(item.getSkuNumber() + "', '");
                query.append(item.getItemDescription() + "', ");
                query.append(item.getPrice() + ")");
                break;
            case "UPDATE":
                query.append("UPDATE " + table + " SET PRICE = '");
                query.append(item.getPrice() + "'");
                query.append("WHERE SKU_NUMBER = '" + item.getSkuNumber() + "'");
                break;
            case "DELETE":
                query.append("DELETE FROM " + table + " WHERE SKU_NUMBER = '");
                query.append(item.getSkuNumber() + "'");
                break;
        }
        log.info("Statement is {}", query);

        // Sends the sql statement to the jdbc:dataSource and Camel INTERNALLY processes the SQL execution.
        exchange.getIn().setBody(query.toString());

    }
}
