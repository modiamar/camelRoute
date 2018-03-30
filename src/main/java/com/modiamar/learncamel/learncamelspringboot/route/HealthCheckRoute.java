package com.modiamar.learncamel.learncamelspringboot.route;

import com.modiamar.learncamel.learncamelspringboot.route.processor.HealthCheckProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HealthCheckRoute extends RouteBuilder {

    private FileConfiguration fileConfiguration;

    @Override
    public void configure() throws Exception {
        from(fileConfiguration.getHealthRoute()).routeId("healthRoute")
            .pollEnrich(fileConfiguration.getHealthEndpoint())
            .process(new HealthCheckProcessor());
    }
}
