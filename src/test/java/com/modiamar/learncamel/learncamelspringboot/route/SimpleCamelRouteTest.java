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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
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

    @Autowired
    @Qualifier("localJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void startCleanUp() throws IOException {
        FileUtils.cleanDirectory(new File("data/input"));
        FileUtils.deleteDirectory(new File("data/output"));
        FileUtils.deleteDirectory(new File("data/input/error"));
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

    @Test
    public void testMoveFiles_ADD() throws InterruptedException, IOException {
        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Flapper Valve,66\n" +
                "ADD,50,Socks,2";
        String fileName = "fileAdd.txt";

        producerTemplate.sendBodyAndHeader(fileConfiguration.getInputFile(), message, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);

        File outfile = new File("data/output/" + fileName);
        assertTrue(outfile.exists());

        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM ITEMS");
        assertTrue(!list.isEmpty());

        String expectedOutput = "Data Updated Successfully";
        byte[] readFilePath = Files.readAllBytes(Paths.get("data/output/Success.txt"));
        String output = new String(readFilePath);
        assertEquals(expectedOutput, output);

    }

    @Test
    public void testMoveFiles_UPDATE() throws InterruptedException, IOException {
        String message = "type,sku#,itemdescription,price\n" +
                "UPDATE,100,Flapper Valve,22";
        String fileName = "fileUpdate.txt";

        producerTemplate.sendBodyAndHeader(fileConfiguration.getInputFile(), message, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);

        File outfile = new File("data/output/" + fileName);
        assertTrue(outfile.exists());

        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM ITEMS");
        assertTrue(!list.isEmpty());

        String expectedOutput = "Data Updated Successfully";
        byte[] readFilePath = Files.readAllBytes(Paths.get("data/output/Success.txt"));
        String output = new String(readFilePath);
        assertEquals(expectedOutput, output);

    }

    @Test
    public void testMoveFiles_DELETE() throws InterruptedException, IOException {
        String message = "type,sku#,itemdescription,price\n" +
                "DELETE,100,Flapper Valve,22";
        String fileName = "fileDelete.txt";

        producerTemplate.sendBodyAndHeader(fileConfiguration.getInputFile(), message, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);

        File outfile = new File("data/output/" + fileName);
        assertTrue(outfile.exists());

        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM ITEMS");
        assertTrue(!list.isEmpty());

        String expectedOutput = "Data Updated Successfully";
        byte[] readFilePath = Files.readAllBytes(Paths.get("data/output/Success.txt"));
        String output = new String(readFilePath);
        assertEquals(expectedOutput, output);

    }

    @Test
    public void testMoveFiles_NoSkuException() throws InterruptedException, IOException {
        String message = "type,sku#,itemdescription,price\n" +
                "ADD,,Flapper Valve,66\n" +
                "ADD,50,Socks,2";
        String fileName = "fileException.txt";

        producerTemplate.sendBodyAndHeader(fileConfiguration.getInputFile(), message, Exchange.FILE_NAME, fileName);
        Thread.sleep(3000);

        File errorFile = new File("data/input/error");
        assertTrue(errorFile.exists());
    }
}