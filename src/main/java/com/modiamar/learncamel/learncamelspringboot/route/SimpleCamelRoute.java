package com.modiamar.learncamel.learncamelspringboot.route;

import com.modiamar.learncamel.learncamelspringboot.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

    private FileConfiguration fileConfiguration;
    private Environment environment;

    public SimpleCamelRoute(FileConfiguration fileConfiguration, Environment environment) {
        this.fileConfiguration = fileConfiguration;
        this.environment = environment;
    }

    @Override
    public void configure() throws Exception {
        log.info("Started the route");

        DataFormat bindy = new BindyCsvDataFormat(Item.class); //Take the class and unMarshal it from the CSV

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
                .end();
        log.info("Ending the camel route");
    }
}
