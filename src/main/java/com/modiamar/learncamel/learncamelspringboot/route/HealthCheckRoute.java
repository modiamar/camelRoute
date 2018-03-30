package com.modiamar.learncamel.learncamelspringboot.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HealthCheckRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from().routeId("healthRoute")
    }
}
