package com.brandonbalala.test.properties;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.test.*;
import com.brandonbalala.properties.MailConfigBean;
import com.brandonbalala.properties.PropertiesManager;;

/**
 * 
 * @author Brandon Balala
 *
 */
public class ManagerTest {
    // A Rule is implemented as a class with methods that are associated
    // with the lifecycle of a unit test. These methods run when required.
    // Avoids the need to cut and paste code into every test method.
    @Rule
    public MethodLogger methodLogger = new MethodLogger();
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

	private PropertiesManager pm;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        pm = new PropertiesManager();
    }

    @Test
    public void testWriteText() throws FileNotFoundException, IOException {
        MailConfigBean mailConfig1 = new MailConfigBean("cs.balala.send@gmail.com", "abc123doremi", "cs.balala.send@gmail.com", "smtp.gmail.com", "imap.gmail.com", "Brandon Balala", 
        		"jdbc:mysql://localhost:", 3306, "EMAIL_DB", "root", "");
        log.info(mailConfig1.toString());
        pm.writeTextProperties("", "TextProps", mailConfig1);

        MailConfigBean mailConfig2 = pm.loadTextProperties("", "TextProps");
        log.info(mailConfig2.toString());

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }

    @Test
    public void testWriteXml() throws FileNotFoundException, IOException {
        MailConfigBean mailConfig1 = new MailConfigBean("cs.balala.send@gmail.com", "abc123doremi", "cs.balala.send@gmail.com", "smtp.gmail.com", "imap.gmail.com", "Brandon Balala", 
        		"jdbc:mysql://localhost:", 3306, "EMAIL_DB", "root", "");
        log.info(mailConfig1.toString());
        pm.writeXmlProperties("", "TextProps", mailConfig1);

        MailConfigBean mailConfig2 = pm.loadXmlProperties("", "TextProps");
        log.info(mailConfig2.toString());

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }
    
    @AfterClass
    public static void deleteTestPropFiles(){
        try {
        	Path path = Paths.get("TextProps.xml");
            Files.delete(path);
        } catch (NoSuchFileException x) {
        	System.out.println(x.getMessage());
        } catch (IOException x) {
            // File permission problems are caught here.
            System.out.println(x.getMessage());
        }
        
        try {
        	Path path = Paths.get("TextProps.properties");
            Files.delete(path);
        } catch (NoSuchFileException x) {
        	System.out.println(x.getMessage());
        } catch (IOException x) {
            // File permission problems are caught here.
            System.out.println(x.getMessage());
        }
    }
    
    @Ignore
    public void testReadJarText() throws NullPointerException, IOException {
        MailConfigBean mailConfig1 = new MailConfigBean("cs.balala.send@gmail.com", "abc123doremi", "cs.balala.send@gmail.com", "smtp.gmail.com", "imap.gmail.com", "Brandon Balala",
        		"jdbc:mysql://localhost:", 3306, "EMAIL_DB", "root", "");
        log.info(mailConfig1.toString());
        
        MailConfigBean mailConfig2 = pm.loadJarTextProperties("testJar.properties");
        log.info(mailConfig2.toString());

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }
}
