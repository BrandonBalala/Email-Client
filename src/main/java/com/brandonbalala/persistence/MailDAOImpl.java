package com.brandonbalala.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailbean.MailBean;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * This class implements the MailDAO
 * 
 * 
 * 
 * @author Brandon Balala
 */
public class MailDAOImpl implements MailDAO {
	
	private final String url = "jdbc:mysql://localhost:3306/EMAIL_DB";

    private final String user = "root";
    private final String password = "";
    
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    
	public MailDAOImpl() {
		super();
	}
	
    /**
     * Method that adds a MailBean object as a record to the database
     * 
     * @param mail
     * @return number of rows created, always 1
     * @throws SQLException
     */
	@Override
	public int createMail(MailBean mail) throws SQLException {
		int result = 0;
		int folderId = 0;
		
		//Creates a folder
		createFolder(mail.getFolder());
		
		//Looks for folder id
		String query = "Select ID FROM FOLDER WHERE NAME = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = connection.prepareStatement(query);) {
			ps.setString(1, mail.getFolder());
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				folderId = rs.getInt("ID");
			}
		}
		
		//Creates row in Mail table
		query = "INSERT INTO MAIL(FROMFIELD, SUBJECT, TEXTMESSAGE, HTMLMESSAGE, DATESENT, DATERECEIVED, FOLDER, STATUS)"
				+ "VALUES (?,?,?,?,?,?,?,?)";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, mail.getFromField());
			ps.setString(2, mail.getSubjectField());
			ps.setString(3, mail.getTextMessageField());
			ps.setString(4, mail.getHTMLMessageField());
			ps.setTimestamp(5, Timestamp.valueOf(mail.getDateSent()));
			ps.setTimestamp(6, Timestamp.valueOf(mail.getDateReceived()));
			ps.setInt(7, folderId);
			ps.setInt(8, mail.getMailStatus());

			result = ps.executeUpdate();
			
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                	mail.setId(rs.getInt(1));
                }
            }
		}
		
		//Create rows in the address table
		query = "INSERT INTO ADDRESS(emailid, email, typeField)"
				+ "VALUES(?,?,?)";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = connection.prepareStatement(query);) {
			
			for(String toField : mail.getToField()){
				ps.setInt(1, mail.getId());
				ps.setString(2, toField);
				ps.setString(3, "TO");
				ps.executeUpdate();
			}
			
			for(String ccField : mail.getCCField()){
				ps.setInt(1, mail.getId());
				ps.setString(2, ccField);
				ps.setString(3, "CC");
				ps.executeUpdate();
			}
			
			for(String bccField : mail.getBCCField()){
				ps.setInt(1, mail.getId());
				ps.setString(2, bccField);
				ps.setString(3, "BCC");
				ps.executeUpdate();
			}
		}
		
		//Create rows in the attachment table
		query = "INSERT INTO ATTACHMENT(emailid, contentid, name, size, content, typeField)"
				+ "VALUES(?,?,?,?,?,?)";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = connection.prepareStatement(query);) {
			
			for(EmailAttachment attach : mail.getAttachField()){
				ps.setInt(1, mail.getId());
				ps.setString(2, "");
				ps.setString(3, attach.getName());
				ps.setInt(4, attach.getSize());
				ps.setBytes(5, attach.toByteArray());
				ps.setString(6, "ATT");
				ps.executeUpdate();
			}
			
			for(EmailAttachment embed : mail.getEmbedField()){
				ps.setInt(1, mail.getId());
				ps.setString(2, embed.getContentId());
				ps.setString(3, embed.getName());
				ps.setInt(4, embed.getSize());
				ps.setBytes(5, embed.toByteArray());
				ps.setString(6, "EMB");
				ps.executeUpdate();
			}
		}
		
		return result;
	}
	
    /**
     * Method that adds a folder name as a record to the database
     * 
     * @param folderName
     * @return number of rows created, always 1
     * @throws SQLException
     */
	@Override
	public int createFolder(String folderName) throws SQLException {
		int result = 0;
		int folderId = 0;
		
		//Find if folder name already exists
		String query = "Select ID FROM FOLDER WHERE NAME = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = connection.prepareStatement(query);) {
			ps.setString(1, folderName);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				folderId = rs.getInt("ID");
			}
		}
		
		//If folder not found in database, it creates a folder with name specified
		if(folderId == 0){
			query = "INSERT INTO FOLDER (NAME)"
					+ "VALUES (?)";
			
			try (Connection connection = DriverManager.getConnection(url, user, password);
					PreparedStatement ps = connection.prepareStatement(query);) {
				
				ps.setString(1, folderName);
	
				result = ps.executeUpdate();
			}
		}
		
		return result;
	}
	
    /**
     * Method that finds mails by subject
     * 
     * @param subject
     * @return arraylist of MailBean objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findMailBySubject(String subject) throws SQLException {
		String query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
				+ " FROM MAIL"
				+ " INNER JOIN FOLDER"
				+ " ON Mail.folder = Folder.id"
				+ " WHERE subject = ?";
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setString(1, subject);
			
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				mailList.add(createMailBean(resultSet));
			}	
		}
			
		return mailList;
	}
	
    /**
     * Method that finds mails by from field
     * 
     * @param email
     * @return arraylist of MailBean objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findMailByFromField(String email) throws SQLException {
		String query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
				+ " FROM MAIL"
				+ " INNER JOIN FOLDER"
				+ " ON Mail.folder = Folder.id"
				+ " WHERE fromField = ?";
	
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setString(1, email);
			
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				mailList.add(createMailBean(resultSet));
			}	
		}
			
		return mailList;
	}
	
    /**
     * Method that finds mails by to field
     * 
     * @param email
     * @return arraylist of MailBean objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findMailByToField(String email) throws SQLException {
		String query = "SELECT emailid"
					+ " FROM ADDRESS"
					+ " WHERE email = ?"
					+ " AND typeField = ?";
		
		ArrayList<Integer> emailId = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setString(1, email);
			ps.setString(2, "TO");
			
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				emailId.add(resultSet.getInt("emailid"));
			}	
		}
		
		if(emailId.size() >= 1){
			query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
					+ " FROM MAIL"
					+ " INNER JOIN FOLDER"
					+ " ON Mail.folder = Folder.id"
					+ " WHERE mail.id = ?";
			
			ObservableList<MailBean> mailList = FXCollections.observableArrayList();
			
			try(Connection connection = DriverManager.getConnection(url, user, password)){
				PreparedStatement ps = connection.prepareStatement(query);	
				
				for(int value : emailId){
					ps.setInt(1, value);
					
					ResultSet resultSet = ps.executeQuery();
					
					while (resultSet.next()) {
						mailList.add(createMailBean(resultSet));
					}	
				}	
			}
				
			return mailList;
		}
		else{
			return FXCollections.observableArrayList();
		}
	}
	
    /**
     * Method that finds mails by cc field
     * 
     * @param email
     * @return arraylist of MailBean objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findMailByCCField(String email) throws SQLException {
		String query = "SELECT emailid"
				+ " FROM ADDRESS"
				+ " WHERE email = ?"
				+ " AND typeField = ?";
	
		ArrayList<Integer> emailId = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setString(1, email);
			ps.setString(2, "CC");
			
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				emailId.add(resultSet.getInt("emailid"));
			}	
		}
		
		if(emailId.size() >= 1){
			query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
					+ " FROM MAIL"
					+ " INNER JOIN FOLDER"
					+ " ON Mail.folder = Folder.id"
					+ " WHERE mail.id = ?";
			
			ObservableList<MailBean> mailList = FXCollections.observableArrayList();
			
			try(Connection connection = DriverManager.getConnection(url, user, password)){
				PreparedStatement ps = connection.prepareStatement(query);	
				
				for(int value : emailId){
					ps.setInt(1, value);
					
					ResultSet resultSet = ps.executeQuery();
					
					while (resultSet.next()) {
						mailList.add(createMailBean(resultSet));
					}	
				}	
			}
				
			return mailList;
		}
		else{
			return FXCollections.observableArrayList();
		}
	}
	
    /**
     * Method that finds mails by bcc field
     * 
     * @param email
     * @return arraylist of MailBean objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findMailByBCCField(String email) throws SQLException {
		String query = "SELECT emailid"
				+ " FROM ADDRESS"
				+ " WHERE email = ?"
				+ " AND typeField = ?";
	
		ArrayList<Integer> emailId = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setString(1, email);
			ps.setString(2, "BCC");
			
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				emailId.add(resultSet.getInt("emailid"));
			}	
		}
		
		if(emailId.size() >= 1){
			query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
					+ " FROM MAIL"
					+ " INNER JOIN FOLDER"
					+ " ON Mail.folder = Folder.id"
					+ " WHERE mail.id = ?";
			
			ObservableList<MailBean> mailList = FXCollections.observableArrayList();
			
			try(Connection connection = DriverManager.getConnection(url, user, password)){
				PreparedStatement ps = connection.prepareStatement(query);	
				
				for(int value : emailId){
					ps.setInt(1, value);
					
					ResultSet resultSet = ps.executeQuery();
					
					while (resultSet.next()) {
						mailList.add(createMailBean(resultSet));
					}	
				}	
			}
				
			return mailList;
		}
		else{
			return FXCollections.observableArrayList();
		}
	}
	
    /**
     * Method that finds mails by date sent
     * 
     * @param dateTime
     * @return arraylist of MailBean objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findMailByDateSent(LocalDateTime dateTime) throws SQLException {
		String query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
				+ " FROM MAIL"
				+ " INNER JOIN FOLDER"
				+ " ON Mail.folder = Folder.id"
				+ " WHERE dateSent = ?";
		
		dateTime = fixNanoSecondProblem(dateTime);
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setTimestamp(1, Timestamp.valueOf(dateTime));
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				mailList.add(createMailBean(resultSet));
			}	
		}
			
		return mailList;
	}

    /**
     * Method that finds mails by date received
     * 
     * @param dateTime
     * @return arraylist of MailBean objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findMailByDateReceived(LocalDateTime dateTime) throws SQLException {
		String query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
				+ " FROM MAIL"
				+ " INNER JOIN FOLDER"
				+ " ON Mail.folder = Folder.id"
				+ " WHERE dateReceived = ?";
		
		dateTime = fixNanoSecondProblem(dateTime);
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setTimestamp(1, Timestamp.valueOf(dateTime));
			
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				mailList.add(createMailBean(resultSet));
			}	
		}
			
		return mailList;
	}

    /**
     * Method that finds mail by id
     * 
     * @param id
     * @return MailBean object
     * @throws SQLException
     */
	@Override
	public MailBean findMailById(int id) throws SQLException {
		String query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
				+ " FROM MAIL"
				+ " INNER JOIN FOLDER"
				+ " ON Mail.folder = Folder.id"
				+ " WHERE mail.id = ?";
	
		MailBean mailBean = new MailBean();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setInt(1, id);
			
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()) {
				mailBean = createMailBean(resultSet);
			}	
		}
			
		return mailBean;
	}
	
    /**
     * Method that finds email address by id
     * 
     * @param id
     * @return String
     * @throws SQLException
     */
	@Override
	public String findEmailAddressById(int id) throws SQLException {
		String query = "SELECT email"
				+ " FROM ADDRESS"
				+ " WHERE ID = ?";
		String email = "";
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()){
				email = resultSet.getString("email");
			}
		}
		
		return email;
	}
	
	/**
	 * Method that finds an attachment by id 
	 * 
	 * @param id
	 * @return EmailAttachment if found else null
	 * @throws SQLException
	 */
	@Override
	public EmailAttachment findAttachmentById(int id) throws SQLException {
		String query = "Select contentId, content, typeField"
				+ " FROM ATTACHMENT"
				+ " WHERE id = ?";
		
		EmailAttachment ea = null;
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setInt(1, id);
			
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				EmailAttachmentBuilder eab;
				if((resultSet.getString("typeField")).equalsIgnoreCase("ATT")){
					eab = EmailAttachment.attachment().bytes(resultSet.getBytes("content"));
					ea = eab.create();
				}
				else{ //else it's an EMB
					eab = EmailAttachment.attachment().bytes(resultSet.getBytes("content")).setInline(resultSet.getString("contentId"));
					ea = eab.create();
				}
			}	
		}
		
		return ea;
	}
	
    /**
     * Method that finds folder name by id
     * 
     * @param int
     * @return String
     * @throws SQLException
     */
	@Override
	public String findFolderNameById(int id) throws SQLException {
		String query = "SELECT name"
				+ " FROM FOLDER"
				+ " WHERE ID = ?";
		String folderName = "";
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()){
				folderName = resultSet.getString("name");
			}
		}
		
		return folderName;
	}
	
    /**
     * Method that finds mail by folder name
     * 
     * @param folderName
     * @return arrayList of MailBean Objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findMailByFolderName(String folderName) throws SQLException {
		String query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
				+ " FROM MAIL"
				+ " INNER JOIN FOLDER"
				+ " ON Mail.folder = Folder.id"
				+ " WHERE name = ?";
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setString(1, folderName);
			
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				mailList.add(createMailBean(resultSet));
			}	
		}
		
		return mailList;
	}
	
    /**
     * Method that finds all folder names and put them into mail beans
     * 
     * @return arrayList of String Objects
     * @throws SQLException
     */
	@Override
	public ObservableList<MailBean> findAllFolder() throws SQLException {
		String query = "SELECT name"
				+ " FROM FOLDER";
	
		ObservableList<MailBean> folderList = FXCollections.observableArrayList();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				MailBean mb = new MailBean();
				mb.setFolder(resultSet.getString("name"));
				folderList.add(mb);
			}	
		}
			
		return folderList;
	}
	
    /**
     * Method that finds all folder names
     * 
     * @return arrayList of String Objects
     * @throws SQLException
     */
	@Override
	public ArrayList<String> findAllFolderNames() throws SQLException {
		String query = "SELECT name"
				+ " FROM FOLDER";
	
		ArrayList<String> folderList = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				folderList.add(resultSet.getString("name"));
			}	
		}
			
		return folderList;
	}
	
	/**
	 * Method that finds all Mail and returns a an ArrayList of all those mail
	 * 
	 * @return arrayList of MailBean objects
	 * @throws SQLException
	 */
	@Override
	public ObservableList<MailBean> findAllMail() throws SQLException {
		String query = "SELECT mail.ID AS ID, fromField, subject, textMessage, htmlMessage, dateSent, dateReceived, name, status"
				+ " FROM MAIL"
				+ " INNER JOIN FOLDER"
				+ " ON Mail.folder = Folder.id";
	
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				mailList.add(createMailBean(resultSet));
			}	
		}
			
		return mailList;
	}
	
    /**
     * Method that updates the name of a folder
     * 
     * @param folderName current name of folder
     * @param newName the name to change to
     * @return int number of rows updated, always 1
     * @throws SQLException
     */
	@Override
	public int update(String folderName, String newName) throws SQLException {
		int result = 0;
		String query = "UPDATE FOLDER"
				+ " SET name = ?"
				+ " WHERE name = ?";
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setString(1, newName);
			ps.setString(2, folderName);
				
			result = ps.executeUpdate();
		}
		
		return result;
	}
	
    /**
     * Method that updates the name of the folder of a specific mailbean
     * 
     * @param mailbean the mailbean to modify
     * @param newName the name to change to
     * @return int number of rows updated, always 1
     * @throws SQLException
     */
	@Override
	public int update(MailBean mailbean, String newFolderName) throws SQLException {
		int result = 0;
		int folderId = 0;
		
		String query = "SELECT folder FROM MAIL"
				+ " WHERE id = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = connection.prepareStatement(query);) {
			ps.setInt(1, mailbean.getId());
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				folderId = rs.getInt("folder");
			}
		}
		
		if(folderId != 0){
			query = "UPDATE FOLDER"
					+ " SET name = ?"
					+ " WHERE id = ?";
			
			try(Connection connection = DriverManager.getConnection(url, user, password)){
				PreparedStatement ps = connection.prepareStatement(query);	
				ps.setString(1, newFolderName);
				ps.setInt(2, folderId);
					
				result = ps.executeUpdate();
			}
		}
		
		return result;
	}
	
    /**
     * Method that deletes a folder.  If deleting folder,
     * it automatically deltes on cascade, which basically removes
     * all the mails that reference it, subsequently deleting the
     * data from Address and Attachment tables
     * 
     * @param name  name of folder
     * @return int number of rows deleted, always 1
     * @throws SQLException
     */
	@Override
	public int deleteFolder(String name) throws SQLException {
		int result = 0;
		String query = "DELETE FROM FOLDER"
				+ " WHERE name = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setString(1, name);
				
			result = ps.executeUpdate();
		}
		
		return result;
	}
	
    /**
     * Method that deletes rows from mail table based on the
     * given id.  Delete cascade is applied, therefore also
     * deleting the date from Address and Attachments tables
     * that references it.
     * 
     * @param id
     * @return int number of rows updated, always 1
     * @throws SQLException
     */
	@Override
	public int deleteMail(int id) throws SQLException {
		int result = 0;
		String query = "DELETE FROM MAIL"
				+ " WHERE id = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setInt(1, id);
				
			result = ps.executeUpdate();
		}
		
		return result;
	}
	
	/**
	 * Private method that creates an object of type MailBean from the current
	 * record in the ResultSet
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private MailBean createMailBean(ResultSet resultSet) throws SQLException {
		
		MailBean mb = new MailBean();
		
		mb.setId(resultSet.getInt("id"));
		mb.setFromField(resultSet.getString("fromField"));
		mb.setSubjectField(resultSet.getString("subject"));
		mb.setTextMessageField(resultSet.getString("textMessage"));
		mb.setHTMLMessageField(resultSet.getString("htmlMessage"));
		mb.setDateSent((resultSet.getTimestamp("dateSent")).toLocalDateTime());
		mb.setDateReceived((resultSet.getTimestamp("dateReceived")).toLocalDateTime());
		mb.setFolder(resultSet.getString("name"));
		mb.setMailStatus(resultSet.getInt("status"));
		
		//Add to, cc and bcc email in the MailBean
		String query = "Select email, typeField FROM ADDRESS "
				+ " WHERE emailid = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setInt(1, mb.getId());
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				if((rs.getString("typeField")).equalsIgnoreCase("TO")){
					mb.getToField().add(rs.getString("email"));
				}
				else if((rs.getString("typeField")).equalsIgnoreCase("CC")){
					mb.getCCField().add(rs.getString("email"));
				}
				else{ //else it's a BCC
					mb.getBCCField().add(rs.getString("email"));
				}
			}	
		}
		
		//Add attachment and embed in the MailBean
		query = "Select contentId, content, typeField"
				+ " FROM ATTACHMENT"
				+ " WHERE emailid = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setInt(1, mb.getId());
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				EmailAttachmentBuilder eab;
				if((rs.getString("typeField")).equalsIgnoreCase("ATT")){
					eab = EmailAttachment.attachment().bytes(rs.getBytes("content"));
					mb.getAttachField().add(eab.create());
				}
				else{ //else it's an EMB
					eab = EmailAttachment.attachment().bytes(rs.getBytes("content")).setInline(rs.getString("contentId"));
					mb.getEmbedField().add(eab.create());
				}
			}	
		}
		
		return mb;
	}
	
	/**
	 * For testing purposes. This looks for attachments by an email id
	 * This was used to check that when Mail row was created that the attachments
	 * were also created in Attachment table.  This was also used when deleting, to see
	 * if cascading delete worked.
	 * 
	 * @param emailid
	 * @return ArrayList of EmailAttachment
	 * @throws SQLException
	 */
	public ArrayList<EmailAttachment> findAttachmentByEmailId(int emailid) throws SQLException {
		String query = "Select contentId, content, typeField"
				+ " FROM ATTACHMENT"
				+ " WHERE emailid = ?";
		
		ArrayList<EmailAttachment> eaList = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);	
			ps.setInt(1, emailid);
			
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				EmailAttachmentBuilder eab;
				if((resultSet.getString("typeField")).equalsIgnoreCase("ATT")){
					eab = EmailAttachment.attachment().bytes(resultSet.getBytes("content"));
					eaList.add(eab.create());
				}
				else{ //else it's an EMB
					eab = EmailAttachment.attachment().bytes(resultSet.getBytes("content")).setInline(resultSet.getString("contentId"));
					eaList.add(eab.create());
				}
			}	
		}
		
		return eaList;
	}
	
	/**
	 * For testing purposes. This looks for an email address by an email id
	 * This was used to check that when Mail row was created that the email
	 * addresses were also created.  This was also used when deleting, to see
	 * if cascading delete worked.
	 * 
	 * @param emailid
	 * @return ArrayList of strings(email addresses)
	 * @throws SQLException
	 */
	public ArrayList<String> findEmailAddressByEmailId(int emailid) throws SQLException {
		String query = "SELECT email"
				+ " FROM ADDRESS"
				+ " WHERE EMAILID = ?";
		ArrayList<String> emailList = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(url, user, password)){
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, emailid);
			
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()){
				emailList.add(resultSet.getString("email"));
			}
		}
		
		return emailList;
	}
	
	/**
	 * I seem to be having problems with nanoseconds.  Nanoseconds aren't kept
	 * in the database.  Seconds are either rounded up or kept as is based on
	 * the nanoseconds.  Uncomment logs in the testFindMailByDateSent() or
	 * testFindMailByDateReceived() to see example 
	 * @param dateTime
	 * @return
	 */
	private LocalDateTime fixNanoSecondProblem(LocalDateTime dateTime){
		//log.debug(Integer.toString(dateTime.getNano()));
		
		//get first digit, lazy way
		String seconds = (Integer.toString(dateTime.getNano())).substring(0, 1);
		if(Integer.parseInt(seconds) < 5){
			dateTime = dateTime.withNano(0);
		}
		else{
			dateTime = dateTime.plusSeconds(1);
			dateTime = dateTime.withNano(0);
		}
		
		return dateTime;
	}
}
