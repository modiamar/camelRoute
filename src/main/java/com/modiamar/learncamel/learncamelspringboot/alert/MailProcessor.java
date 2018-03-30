package com.modiamar.learncamel.learncamelspringboot.alert;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailProcessor implements Processor {

    private JavaMailSender emailSender;
    private Environment environment;

    public MailProcessor(JavaMailSender emailSender, Environment environment) {
        this.emailSender = emailSender;
        this.environment = environment;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Exception e = exchange.getProperty(exchange.EXCEPTION_CAUGHT, Exception.class);
        log.info("Exception that was caught was {}", e.getMessage());

        String bodyMessage = "Exception occured in the Camel Route and it was : " + e.getMessage();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(environment.getProperty("spring.mail.mailFrom"));
        simpleMailMessage.setTo(environment.getProperty("spring.mail.mailTo"));
        simpleMailMessage.setSubject("Exception in Camel Route");
        simpleMailMessage.setText(bodyMessage);

        emailSender.send(simpleMailMessage);
    }
}
