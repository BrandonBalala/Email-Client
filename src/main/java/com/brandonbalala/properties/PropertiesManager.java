package com.brandonbalala.properties;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.brandonbalala.properties.*;

/**
 * 
 * @author 1337762
 *
 */

public class PropertiesManager {
	
	public final MailConfigBean loadTextProperties(final String path, final String propFileName) throws IOException {
		Properties prop = new Properties();

		Path txtFile = get(path, propFileName + ".properties");

		MailConfigBean mailConfig = new MailConfigBean();

		// File must exist
		if (Files.exists(txtFile)) {
			try (InputStream propFileStream = newInputStream(txtFile);) {
				prop.load(propFileStream);
			}
			mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
			mailConfig.setPassword(prop.getProperty("password"));
			mailConfig.setUsername(prop.getProperty("username"));
			mailConfig.setSmtp(prop.getProperty("smtp"));
			mailConfig.setImap(prop.getProperty("imap"));
			mailConfig.setUrl(prop.getProperty("url"));
			mailConfig.setFullName(prop.getProperty("fullName"));
			mailConfig.setPort(Integer.parseInt(prop.getProperty("port")));
			mailConfig.setDatabase(prop.getProperty("database"));
			mailConfig.setDbUsername(prop.getProperty("dbUsername"));
			mailConfig.setDbPassword(prop.getProperty("dbPassword"));
		}
		return mailConfig;
	}
	
	public final void writeTextProperties(final String path, final String propFileName, final MailConfigBean mailConfig) throws IOException {

		Properties prop = new Properties();

		prop.setProperty("userEmailAddress", mailConfig.getUserEmailAddress());
		prop.setProperty("password", mailConfig.getPassword());
		prop.setProperty("username", mailConfig.getUsername());
		prop.setProperty("smtp", mailConfig.getSmtp());
		prop.setProperty("imap", mailConfig.getImap());
		prop.setProperty("fullName", mailConfig.getFullName());
		prop.setProperty("url", mailConfig.getUrl());
		prop.setProperty("port", Integer.toString(mailConfig.getPort()));
		prop.setProperty("database", mailConfig.getDatabase());
		prop.setProperty("dbUsername", mailConfig.getDbUsername());
		prop.setProperty("dbPassword", mailConfig.getDbPassword());
		

		Path txtFile = get(path, propFileName + ".properties");

		// Creates the file or if file exists it is truncated to length of zero
		// before writing
		try (OutputStream propFileStream = newOutputStream(txtFile)) {
			prop.store(propFileStream, "SMTP Properties");
		}
	}
	
	public final MailConfigBean loadXmlProperties(final String path, final String propFileName) throws IOException {

		Properties prop = new Properties();

		// The path of the XML file
		Path xmlFile = get(path, propFileName + ".xml");

		MailConfigBean mailConfig = new MailConfigBean();

		// File must exist
		if (Files.exists(xmlFile)) {
			try (InputStream propFileStream = newInputStream(xmlFile);) {
				prop.loadFromXML(propFileStream);
			}
			mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
			mailConfig.setPassword(prop.getProperty("password"));
			mailConfig.setUsername(prop.getProperty("username"));
			mailConfig.setSmtp(prop.getProperty("smtp"));
			mailConfig.setImap(prop.getProperty("imap"));
			mailConfig.setFullName(prop.getProperty("fullName"));
			mailConfig.setUrl(prop.getProperty("url"));
			mailConfig.setPort(Integer.parseInt(prop.getProperty("port")));
			mailConfig.setDatabase(prop.getProperty("database"));
			mailConfig.setDbUsername(prop.getProperty("dbUsername"));
			mailConfig.setDbPassword(prop.getProperty("dbPassword"));
		}
		return mailConfig;
	}
	
	public final void writeXmlProperties(final String path, final String propFileName, final MailConfigBean mailConfig) throws IOException {

		Properties prop = new Properties();

		prop.setProperty("userEmailAddress", mailConfig.getUserEmailAddress());
		prop.setProperty("password", mailConfig.getPassword());
		prop.setProperty("username", mailConfig.getUsername());
		prop.setProperty("smtp", mailConfig.getSmtp());
		prop.setProperty("imap", mailConfig.getImap());
		prop.setProperty("fullName", mailConfig.getFullName());
		prop.setProperty("url", mailConfig.getUrl());
		prop.setProperty("port", Integer.toString(mailConfig.getPort()));
		prop.setProperty("database", mailConfig.getDatabase());
		prop.setProperty("dbUsername", mailConfig.getDbUsername());
		prop.setProperty("dbPassword", mailConfig.getDbPassword());

		Path xmlFile = get(path, propFileName + ".xml");

		// Creates the file or if file exists it is truncated to length of zero
		// before writing
		try (OutputStream propFileStream = newOutputStream(xmlFile)) {
			prop.storeToXML(propFileStream, "XML SMTP Properties");
		}
	}
	
	public final MailConfigBean loadJarTextProperties(final String propertiesFileName) throws IOException, NullPointerException {
		
		Properties prop = new Properties();
		MailConfigBean mailConfig = new MailConfigBean();

		// There is no exists method for files in a jar so we try to get the
		// resource and if its not there it returns a null
		if (this.getClass().getResource("/" + propertiesFileName) != null) {
			// Assumes that the properties file is in the root of the
			// project/jar.
			// Include a path from the root if required.
			try (InputStream stream = this.getClass().getResourceAsStream("/" + propertiesFileName);) {
				prop.load(stream);
			}
			mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
			mailConfig.setPassword(prop.getProperty("password"));
			mailConfig.setUsername(prop.getProperty("username"));
			mailConfig.setSmtp(prop.getProperty("smtp"));
			mailConfig.setImap(prop.getProperty("imap"));
			mailConfig.setFullName(prop.getProperty("fullName"));
			mailConfig.setUrl(prop.getProperty("url"));
			mailConfig.setPort(Integer.parseInt(prop.getProperty("port")));
			mailConfig.setDatabase(prop.getProperty("database"));
			mailConfig.setDbUsername(prop.getProperty("dbUsername"));
			mailConfig.setDbPassword(prop.getProperty("dbPassword"));
		}
		return mailConfig;
	}
	
	/**
	private String userEmailAddress;
	private String password;
	private String username;
	private String smtp;
	private String imap;
	
	private String url;
	private int port;
	private String database;
	private String dbUsername;
	private String dbPassword;
	*/
	
	
	
	/**
	 * NOTES PROPERTIES
	 * 
	 * MAIL
	 * - S Username => username might be an email address depending on service [x]
	 * - S Email address [x]
	 * - S Name => FULL NAME [x]
	 * - S Password [x]
	 * - S SMTP []
	 * - S IMAP []
	 * - SMTP PORT (OPTIONAL, JODD DOESNT ASK FOR PORT#)
	 * - IMAP PORT (OPTIONAL, JODD DOESNT ASK FOR PORT#)
	 * 
	 * DATABASE (is a my sql protocol)
	 * - PROTOCOL (remove these because always jdbc:mysql//)
	 * - SUBPROTOCOL (remove these because always "jdbc:mysql//")
	 * - S URL [x]
	 * - INT PORT [x]
	 * - S DATABASE [x]
	 * - S USERNAME [x]
	 * - S PASSWORD [x]
	 * 
	 * WHEN PROGRAM BEGINS
	 * => READ PROPERTIES MAIL
	 *  =>IF DOES NOT EXIST
	 * 		-display form with all these fields
	 * 		-saveproperties
	 * 
	 */
}
