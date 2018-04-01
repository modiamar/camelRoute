package com.modiamar.learncamel.learncamelkafka.routes;

import com.modiamar.learncamel.learncamelspringboot.route.SimpleCamelRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleRoute {

    @Autowired
    private SimpleCamelRoute simpleCamelRoute;
}
