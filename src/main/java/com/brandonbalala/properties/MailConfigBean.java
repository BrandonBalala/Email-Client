package com.brandonbalala.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class to contain the information for an email account. This is sufficient for
 * this project but will need more fields if you wish the program to work with
 * mail systems other than GMail. This should be stored in properties file. If
 * you are feeling adventurous you can look into how you might encrypt the
 * password as it will be in a simple text file.
 * 
 * @author Brandon Balala
 * 
 */
public class MailConfigBean {
	//FOR MAIL
	private StringProperty userEmailAddress;
	private StringProperty password;
	private StringProperty username;
	private StringProperty smtp;
	private StringProperty imap;
	private StringProperty fullName;
	
	//FOR DATABASE
	private StringProperty url;
	private IntegerProperty port;
	private StringProperty database;
	private StringProperty dbUsername;
	private StringProperty dbPassword;
	
	/**
	 * Default Constructor
	 */
	public MailConfigBean() {
		this("", "", "", "", "", "", "", 0, "", "", "");
	}

	/**
	 * Constructor with Java primitive types
	 * @param userEmailAddress
	 * @param password
	 * @param username
	 * @param smtp
	 * @param imap
	 * @param fullName
	 * @param url
	 * @param port
	 * @param database
	 * @param dbUsername
	 * @param dbPassword
	 */
	public MailConfigBean(final String userEmailAddress, final String password, final String username, final String smtp, final String imap, final String fullName, 
								final String url, final int port, final String database, final String dbUsername, final String dbPassword) {
		System.out.println("CREATING MAILCONFIGBEAN");
		this.userEmailAddress = new SimpleStringProperty(userEmailAddress);
		this.password = new SimpleStringProperty(password);
		this.username = new SimpleStringProperty(username);
		this.smtp = new SimpleStringProperty(smtp);
		this.imap = new SimpleStringProperty(imap);
		this.fullName = new SimpleStringProperty(fullName);
		this.url = new SimpleStringProperty(url);
		this.port = new SimpleIntegerProperty(port);
		this.database = new SimpleStringProperty(database);
		this.dbUsername = new SimpleStringProperty(dbUsername);
		this.dbPassword = new SimpleStringProperty(dbPassword);
	}
	
	/**
	 * Constructor with JavaFX Properties
	 * @param userEmailAddress
	 * @param password
	 * @param username
	 * @param smtp
	 * @param imap
	 * @param fullName
	 * @param url
	 * @param port
	 * @param database
	 * @param dbUsername
	 * @param dbPassword
	 */
	public MailConfigBean(final StringProperty userEmailAddress, final StringProperty password, final StringProperty username, final StringProperty smtp, final StringProperty imap, final StringProperty fullName, 
								final StringProperty url, final IntegerProperty port, final StringProperty database, final StringProperty dbUsername, final StringProperty dbPassword) {
		super();
		this.userEmailAddress = userEmailAddress;
		this.password = password;
		this.username = username;
		this.smtp = smtp;
		this.imap = imap;
		this.fullName = fullName;
		this.url = url;
		this.port = port;
		this.database = database;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
	}

	/**
	 * @return the userName
	 */
	public final String getUserEmailAddress() {
		return userEmailAddress.get();
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public final void setUserEmailAddress(final String userEmailAddress) {
		this.userEmailAddress.set(userEmailAddress);
	}
	
	/**
	 * @return the userName
	 */
	public final StringProperty userEmailAddressProperty() {
		return userEmailAddress;
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password.get();
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public final void setPassword(final String password) {
		this.password.set(password);
	}
	
	/**
	 * @return the password
	 */
	public final StringProperty passwordProperty() {
		return password;
	}
	
	/**
	 * @return the username
	 */
	public final String getUsername() {
		return username.get();
	}
	
	/**
	 * @param username
	 *            the username to set
	 */
	public final void setUsername(final String username) {
		this.username.set(username);
	}
	
	/**
	 * @return the username
	 */
	public final StringProperty usernameProperty() {
		return username;
	}
	
	/**
	 * @return the smtp host
	 */
	public final String getSmtp() {
		return smtp.get();
	}
	
	/**
	 * @param smtp
	 *            the smtp host to set
	 */
	public final void setSmtp(final String smtp) {
		this.smtp.set(smtp);
	}
	
	/**
	 * @return the smtp host
	 */
	public final StringProperty smtpProperty() {
		return smtp;
	}
	
	/**
	 * @return the imap host
	 */
	public final String getImap() {
		return imap.get();
	}

	/**
	 * @param imap
	 *            the imap host to set
	 */
	public final void setImap(final String imap) {
		this.imap.set(imap);
	}
	
	/**
	 * @return the imap host
	 */
	public final StringProperty imapProperty() {
		return imap;
	}
	
	/**
	 * @return the fullName
	 */
	public final String getFullName() {
		return fullName.get();
	}

	/**
	 * @param imap
	 *            the fullName to set
	 */
	public final void setFullName(final String fullName) {
		this.fullName.set(fullName);
	}
	
	/**
	 * @return the fullName
	 */
	public final StringProperty fullNameProperty() {
		return fullName;
	}
	
	/**
	 * @return the url
	 */
	public final String getUrl() {
		return url.get();
	}
	
	/**
	 * @param url
	 *            the url to set
	 */
	public final void setUrl(final String url) {
		this.url.set(url);
	}
	
	/**
	 * @return the url
	 */
	public final StringProperty urlProperty() {
		return url;
	}
	
	/**
	 * @return the port number
	 */
	public final int getPort() {
		return port.get();
	}
	
	/**
	 * @param port
	 *            the port number to set
	 */
	public final void setPort(final int port) {
		this.port.set(port);
	}
	
	/**
	 * @return the port number
	 */
	public final IntegerProperty portProperty() {
		return port;
	}
	
	/**
	 * @return the database name
	 */
	public final String getDatabase() {
		return database.get();
	}
	
	/**
	 * @param database
	 *            the database name to set
	 */
	public final void setDatabase(final String database) {
		this.database.set(database);
	}
	
	/**
	 * @return the database name
	 */
	public final StringProperty databaseProperty() {
		return database;
	}
	
	/**
	 * @return the database username
	 */
	public final String getDbUsername() {
		return dbUsername.get();
	}

	/**
	 * @param dbUsername
	 *            the database username to set
	 */
	public final void setDbUsername(final String dbUsername) {
		this.dbUsername.set(dbUsername);
	}
	
	/**
	 * @return the database username
	 */
	public final StringProperty dbUsernameProperty() {
		return dbUsername;
	}
	
	/**
	 * @return the database password
	 */
	public final String getDbPassword() {
		return dbPassword.get();
	}

	/**
	 * @param dbPassword
	 *            the database password to set
	 */
	public final void setDbPassword(final String dbPassword) {
		this.dbPassword.set(dbPassword);
	}
	
	/**
	 * @return the database password
	 */
	public final StringProperty dbPasswordProperty() {
		return dbPassword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((database.get() == null) ? 0 : database.get().hashCode());
		result = prime * result + ((dbPassword.get() == null) ? 0 : dbPassword.get().hashCode());
		result = prime * result + ((dbUsername.get() == null) ? 0 : dbUsername.get().hashCode());
		result = prime * result + ((imap.get() == null) ? 0 : imap.get().hashCode());
		result = prime * result + ((fullName.get() == null) ? 0 : fullName.get().hashCode());
		result = prime * result + ((password.get() == null) ? 0 : password.get().hashCode());
		//result = prime * result + ((port.get() == null) ? 0 : port.get().hashCode());
		result = prime * result + ((smtp.get() == null) ? 0 : smtp.get().hashCode());
		result = prime * result + ((url.get() == null) ? 0 : url.get().hashCode());
		result = prime * result + ((userEmailAddress.get() == null) ? 0 : userEmailAddress.get().hashCode());
		result = prime * result + ((username.get() == null) ? 0 : username.get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Logger log = LoggerFactory.getLogger(getClass().getName());
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MailConfigBean other = (MailConfigBean) obj;
		
		//Database Name
		if (database.get() == null) {
			if (other.database.get() != null){
				log.debug("database name do not match");
				return false;
		}
		} else if (!database.get().equals(other.database.get())){
			log.debug("database name do not match");
			return false;
		}
		
		//Database Password
		if (dbPassword.get() == null) {
			if (other.dbPassword.get() != null){
				log.debug("database password name do not match");
				return false;
			}
		} else if (!dbPassword.get().equals(other.dbPassword.get())){
			log.debug("database password do not match");
			return false;
		}
		
		//Database Username
		if (dbUsername.get() == null) {
			if (other.dbUsername.get() != null){
				log.debug("database username do not match");
				return false;
			}
		} else if (!dbUsername.get().equals(other.dbUsername.get())){
			log.debug("database username do not match");
			return false;
		}
		
		//IMAP
		if (imap.get() == null) {
			if (other.imap.get() != null){
				log.debug("imap server do not match");
				return false;
			}
		} else if (!imap.get().equals(other.imap.get())){
			log.debug("imap server do not match");
			return false;
		}
		
		//Full Name
		if (fullName.get() == null) {
			if (other.fullName.get() != null){
				log.debug("full name do not match");
				return false;
			}
		} else if (!fullName.get().equals(other.fullName.get())){
			log.debug("full name do not match");
			return false;
		}
		
		//Email password
		if (password.get() == null) {
			if (other.password.get() != null){
				log.debug("password do not match");
				return false;
			}
		} else if (!password.get().equals(other.password.get())){
			log.debug("password do not match");
			return false;
		}
		
		//Port number
		if (port.get() != other.port.get()){
			log.debug("port number do not match");
			return false;
		}
		
		//SMTP server
		if (smtp.get() == null) {
			if (other.smtp.get() != null){
				log.debug("smtp server do not match");
				return false;
			}
		} else if (!smtp.get().equals(other.smtp.get())){
			log.debug("smtp server do not match");
			return false;
		}
		
		//Database url
		if (url.get() == null) {
			if (other.url.get() != null){
				log.debug("url do not match");
				return false;
			}
		} else if (!url.get().equals(other.url.get())){
			log.debug("url do not match");
			return false;
		}
		
		//User email address
		if (userEmailAddress.get() == null) {
			if (other.userEmailAddress.get() != null){
				log.debug("user email address do not match");
				return false;
			}
		} else if (!userEmailAddress.get().equals(other.userEmailAddress.get())){
			log.debug("user email address do not match");
			return false;
		}
		
		//Database username
		if (username.get() == null) {
			if (other.username.get() != null){
				log.debug("database username do not match");
				return false;
			}
		} else if (!username.get().equals(other.username.get())){
			log.debug("database username do not match");
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return "MailConfigBean [userEmailAddress=" + userEmailAddress.get() + ", password=" + password.get() + ", username="
				+ username.get() + ", smtp=" + smtp.get() + ", imap=" + imap.get() + ", url=" + url.get() + ", port=" + port.get() + ", database="
				+ database.get() + ", dbUsername=" + dbUsername.get() + ", dbPassword=" + dbPassword.get() + "]";
	}
}