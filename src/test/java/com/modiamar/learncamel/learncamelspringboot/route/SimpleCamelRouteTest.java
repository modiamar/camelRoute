package com.modiamar.learncamel.learncamelspringboot.route;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

@RunWith(CamelSpringBootRunner.class)
@ActiveProfiles("dev")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  //Can decide when to reload everything
public class SimpleCamelRouteTest {

    @Autowired
    ProducerTemplate producerTemplate; //Produce some files Need File Content/File Name

    @Autowired
    FileConfiguration fileConfiguration;

    @BeforeClass
    public static void startCleanUp() throws IOException {
        FileUtils.cleanDirectory(new File("data/input"));
        FileUtils.deleteDirectory(new File("data/output"));
    }

    @Test
    public void testMoveFile() throws InterruptedException {
        /**
         * Will start application and then pass this file into the input directory
         */
        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Flapper Valve,66\n" +
                "ADD,50,Socks,2";
        String fileName = "fileTest.txt";
        producerTemplate.sendBodyAndHeader(fileConfiguration.getInputFile(), message, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);
        File outfile = new File("data/output/" + fileName);
        assertTrue(outfile.exists());
    }


}