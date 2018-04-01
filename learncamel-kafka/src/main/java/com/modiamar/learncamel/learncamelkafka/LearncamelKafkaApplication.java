package com.modiamar.learncamel.learncamelkafka;

import com.modiamar.learncamel.learncamelspringboot.LearncamelSpringbootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearncamelKafkaApplication extends LearncamelSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearncamelKafkaApplication.class, args);
	}
}
