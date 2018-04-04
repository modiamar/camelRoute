package com.modiamar.learncamel.learncamelspringboot.route;

import com.modiamar.learncamel.learncamelspringboot.alert.MailProcessor;
import com.modiamar.learncamel.learncamelspringboot.route.processor.HealthCheckProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
//@Slf4j
public class HealthCheckRoute extends RouteBuilder {

    private FileConfiguration fileConfiguration;
    private HealthCheckProcessor healthCheckProcessor;
    private MailProcessor mailProcessor;
    private Predicate isNotMock = header("env").isNotEqualTo("mock");

    /**
     * Would like to use this constructor on startup
     *
     * @param fileConfiguration
     * @param healthCheckProcessor
     * @param mailProcessor
     */
    @Autowired
    public HealthCheckRoute(FileConfiguration fileConfiguration, HealthCheckProcessor healthCheckProcessor, MailProcessor mailProcessor) {
        this.fileConfiguration = fileConfiguration;
        this.healthCheckProcessor = healthCheckProcessor;
        this.mailProcessor = mailProcessor;
    }

    /**
     * Uses this constructor to create the beans by default
     */
    public HealthCheckRoute() {
    }

    @Override
    public void configure() throws Exception {
        from(fileConfiguration.getHealthRoute()).routeId("healthRoute")
                .choice()
                    .when(isNotMock)
                        .pollEnrich(fileConfiguration.getHealthEndpoint())
                .end()
                .process(healthCheckProcessor)
                .choice()
                    .when(header("error").isEqualTo(true))
                        .choice()
                            .when(isNotMock)
                                .process(mailProcessor)
                        .end()
                .end();
    }
}
