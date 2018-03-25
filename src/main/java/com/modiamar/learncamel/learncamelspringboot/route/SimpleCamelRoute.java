package com.modiamar.learncamel.learncamelspringboot.route;

import com.modiamar.learncamel.learncamelspringboot.domain.Item;
import com.modiamar.learncamel.learncamelspringboot.route.exceptions.DataException;
import com.modiamar.learncamel.learncamelspringboot.route.processor.BuildSQLProcessor;
import com.modiamar.learncamel.learncamelspringboot.route.processor.SuccessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

    private FileConfiguration fileConfiguration;
    private Environment environment;
    private BuildSQLProcessor buildSQLProcessor;
    private SuccessProcessor successProcessor;
    private DataSource dataSource;

    public SimpleCamelRoute(FileConfiguration fileConfiguration, Environment environment, BuildSQLProcessor buildSQLProcessor,
                            SuccessProcessor successProcessor, DataSource dataSource) {
        this.fileConfiguration = fileConfiguration;
        this.environment = environment;
        this.buildSQLProcessor = buildSQLProcessor;
        this.successProcessor = successProcessor;
        this.dataSource = dataSource;
    }

    @Override
    public void configure() throws Exception {
        log.info("Started the route");

        DataFormat bindy = new BindyCsvDataFormat(Item.class); //Take the class and unMarshal it from the CSV

        //We are telling Camel to use the deadLetterChannel instead of Default Handler
        // Handle this exception, BUT dont stop the route
        // If retry, then it WILL stop the route
//        errorHandler(deadLetterChannel("log:errorInRoute?level=ERROR&showProperties=true")
//                            .maximumRedeliveries(3)
//                            .redeliveryDelay(3000)
//                            .backOffMultiplier(2)
//                            .retryAttemptedLogLevel(LoggingLevel.ERROR));

        onException(DataException.class).log(LoggingLevel.ERROR, "Exception in the route ${body}");

        from(fileConfiguration.getTimerTime())
                .log("Timer invoked and the body is " + environment.getProperty("message"))
                .choice() // if statement
                    .when(header("env").isNotEqualTo("mock")) //When the env is not equal to mock do this This is checking the header we set in test case
                        .pollEnrich(fileConfiguration.getInputFile()) //Does this from scratch
                    .otherwise()
                        .log("mock is running. Body is ${body}")
                .end() //ends the choice
                .to(fileConfiguration.getOutputFile())
                .unmarshal(bindy)
                .log("Unmarshalled object is ${body}") // Will print out the POJO (each)
                .split(body())
                    .log("Record is ${body}")
                    .process(buildSQLProcessor) //This is the object we created implements Processor DOES ALL SQL Logic
                    .to(fileConfiguration.getJdbcDataSource()) //REMEMBER: This needs to be the Bean name you defined!
                .end()
        .process(successProcessor)
        .to(fileConfiguration.getSuccessRoute());
        log.info("Ending the camel route");
    }
}
