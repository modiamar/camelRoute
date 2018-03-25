package com.modiamar.learncamel.learncamelspringboot.route;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix=FileConfiguration.PREFIX)
public class FileConfiguration {

    public static final String PREFIX = "spring.camel.external.properties";

    private String inputFile;

    private String outputFile;

    private String timerTime;

    private String jdbcDataSource;

    private String successRoute;

    public String getSuccessRoute() {
        return successRoute;
    }

    public void setSuccessRoute(String successRoute) {
        this.successRoute = successRoute;
    }

    public String getJdbcDataSource() {
        return jdbcDataSource;
    }

    public void setJdbcDataSource(String jdbcDataSource) {
        this.jdbcDataSource = jdbcDataSource;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getTimerTime() {
        return timerTime;
    }

    public void setTimerTime(String timerTime) {
        this.timerTime = timerTime;
    }
}
