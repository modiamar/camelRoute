package com.modiamar.learncamel.learncamelspringboot.route;

import com.modiamar.learncamel.learncamelspringboot.alert.MailProcessor;
import com.modiamar.learncamel.learncamelspringboot.domain.KafkaItem;
import com.modiamar.learncamel.learncamelspringboot.processor.ValidateDataProcessor;
import com.modiamar.learncamel.learncamelspringboot.route.exceptions.DataException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class KafkaSimpleCamelRoute extends RouteBuilder {

    private Environment environment;
    private FileConfiguration fileConfiguration;
    private DataSource dataSource;
    private MailProcessor mailProcessor;
    private ValidateDataProcessor validateDataProcessor;

    @Autowired
    public KafkaSimpleCamelRoute(Environment environment, FileConfiguration fileConfiguration, DataSource dataSource, MailProcessor mailProcessor, ValidateDataProcessor validateDataProcessor) {
        this.environment = environment;
        this.fileConfiguration = fileConfiguration;
        this.dataSource = dataSource;
        this.mailProcessor = mailProcessor;
        this.validateDataProcessor = validateDataProcessor;
    }

    public KafkaSimpleCamelRoute(){}

    @Override
    public void configure() throws Exception {

        //Checks for the header and sees if it is mock or NOT
        Predicate isNotMock = header("env").isNotEqualTo("mock");

        // Unmarshalls the json into a JAVA object
        GsonDataFormat kafkaItemFormat = new GsonDataFormat(KafkaItem.class);

        onException(PSQLException.class).log(LoggingLevel.ERROR,"PSQLException in the route ${body}")
                .maximumRedeliveries(3)
                .redeliveryDelay(3000)
                .backOffMultiplier(2)
                .retryAttemptedLogLevel(LoggingLevel.ERROR);

        onException(DataException.class).log(LoggingLevel.ERROR, "DataException in the route ${body}")
                .choice()
                    .when(isNotMock)
                        .process(mailProcessor)
                .end()
                .to(environment.getProperty("errorRoute"));

        from("{{fromRoute}}")
                .log("Read message from Kafka is ${body}")
                .unmarshal(kafkaItemFormat)
                    .log("Unmarshalled message from Kafka is ${body}")
                .process(validateDataProcessor)
                .to("{{toRoute}}");

    }
}
