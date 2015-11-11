package com.brandonbalala.test.persistence;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAOImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;
/**
 * A series of test that determines if CRUD methods work as expected to
 * 
 * Catching exception in tests simply to print stack trace, when I had errors
 * Might wan't to continue throwing the exception when using these methods
 * @author Brandon Balala
 *
 */
public class MailDAOTest {
	// Temporary will be putting these in properties file
	private final String url = "jdbc:mysql://localhost:3306/";
	private final String user = "root";
	private final String password = "";
	
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    
	/**
	 * This method tests adding a row in the the folder table
	 */
	
	@Test
	public void createFolder() {
		MailDAOImpl md = new MailDAOImpl();
		int rowsCreated = -1;
		try {
			rowsCreated = md.createFolder("TestFolder");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have created 1 row
		assertEquals("Folder not created", 1, rowsCreated);
	} 
	
	/**
	 * This method tests that it does not add a row in the folder table
	 * if the the folder name already exists.
	 */
	
	@Test
	public void createDuplicateFolder() {
		MailDAOImpl md = new MailDAOImpl(); 
		int rowsCreated = -1;
		
		try {
			md.createFolder("TestDuplicate");
			//Adding a duplicate
			rowsCreated = md.createFolder("TestDuplicate");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have created zero rows because it already exists
		assertEquals("Error created a duplicate", 0, rowsCreated);
	}
	
	/**
	 * This method tests that the create feature by sending a simple mail,
	 * without attachments
	 */
	
	@Test
	public void createSimpleMail() {
		MailDAOImpl md = new MailDAOImpl(); 
		MailBean foundMB = new MailBean();
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		try {
			md.createMail(mailBean);
			foundMB = md.findMailById(mailBean.getId());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, foundMB);
	}
	
	/**
	 * This method tests that the create feature by sending a simple mail,
	 * without attachments
	 */
	
	@Test
	public void createMultipleSimpleMail() {
		MailDAOImpl md = new MailDAOImpl(); 
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");

		int rowsCreated = -1;
		
		try {
			rowsCreated = md.createMail(mailBean);
			rowsCreated += md.createMail(mailBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have created 2 email in the MAIL table
		assertEquals("Mails were not created", 2, rowsCreated);
	}
	
	/**
	 * This method tests that the create feature by sending a MailBean containing an HTML message,
	 * without attachments
	 */
	
	@Test
	public void createHTMLMail() {
		MailDAOImpl md = new MailDAOImpl(); 
		MailBean foundMB = new MailBean();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setHTMLMessageField("<h1>TEST</h1>");
		mailBean.setFolder("TEST");
		
		try {
			md.createMail(mailBean);
			foundMB = md.findMailById(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, foundMB);
	}
	
	/**
	 * This method tests that the create feature by sending a MailBean with CC and BCC,
	 * without attachments
	 */
	
	@Test
	public void createMailWithCCandBCC() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean foundMB = new MailBean();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getCCField().add("TEST@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		try {
			md.createMail(mailBean);
			foundMB = md.findMailById(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have created an email in the MAIL table
		assertEquals("MailBeans are not the same", mailBean, foundMB);
	}
	
	/**
	 * This method tests that the create feature by sending a MailBean
	 * containing one embedded element
	 */
	
	@Test
	public void createMailWithEmbed() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean foundMB = new MailBean();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBean.getEmbedField().add(ea);
		
		try {
			md.createMail(mailBean);
			foundMB = md.findMailById(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, foundMB);
	}
	
	/**
	 * This method tests that the create feature by sending a MailBean
	 * containing one attach element
	 */
	
	@Test
	public void createMailWithAttach() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean foundMB = new MailBean();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		try {
			md.createMail(mailBean);
			foundMB = md.findMailById(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, foundMB);
	}
	
	/**
	 * This method tests that the create feature by sending a MailBean
	 * containing one attach element
	 */
	
	@Test
	public void createMailWithMultipleAttachAndEmbed() {
		MailDAOImpl md = new MailDAOImpl(); 
		MailBean foundMB = new MailBean();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("TEST");
		mailBean.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBean.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("turtle.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("pyramid.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		try {
			md.createMail(mailBean);
			foundMB = md.findMailById(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, foundMB);
	}
	
	/**
	 * This method tests the create feature by sending MailBean
	 * containing many attachment. This checks that the attachments where
	 * in fact created in the attachment table and the email addressses were
	 * created in the Address table
	 */
	
	@Test
	public void createMailCheckAttachmentsAndAddress() {
		MailDAOImpl md = new MailDAOImpl(); 
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("TEST");
		mailBean.setFolder("TESTING");
		mailBean.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBean.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("turtle.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("pyramid.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		int rowsCreated = -1;
		ArrayList<String> emailAddressFound = new ArrayList<>();
		ArrayList<EmailAttachment> attachmentFound = new ArrayList<>();
		
		try {
			rowsCreated = md.createMail(mailBean);
			
			emailAddressFound = md.findEmailAddressByEmailId(mailBean.getId());
			attachmentFound = md.findAttachmentByEmailId(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have created 2 email in the MAIL table
		assertEquals("Mails were not created", 1, rowsCreated);
		assertEquals("Does not work", 1, emailAddressFound.size());
		assertEquals("Does not work", 4, attachmentFound.size());
	}
	
	/**
	 * This method tests deleting folders
	 */
	
	@Test
	public void deleteFolder() {
		MailDAOImpl md = new MailDAOImpl();
		int rowsDeleted = -1;
		try {
			//Create folder
			md.createFolder("TestFolder");
			
			
			rowsDeleted = md.deleteFolder("TestFolder");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have deleted 1 row
		assertEquals("Folder was not deleted", 1, rowsDeleted);
	}
	
	/**
	 * This method tries to delete a folder that does not exist
	 */
	
	@Test
	public void tryDeletingFolderThatDoesNotExist() {
		MailDAOImpl md = new MailDAOImpl();
		int rowsDeleted = -1;
		try {
			//Create folder
			md.createFolder("TestFolder");
			
			
			rowsDeleted = md.deleteFolder("WHATEVER");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have deleted 0 rows
		assertEquals("Folder was not deleted", 0, rowsDeleted);
		
	}
	
	/**
	 * This method tries to delete a folder. This test is specifically to check
	 * that on delete cascade works
	 */
	@Test
	public void tryDeletingFolderCascade() {
		MailDAOImpl md = new MailDAOImpl(); 
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("TEST");
		mailBean.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBean.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("turtle.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("pyramid.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		mailBean.setFolder("TEST");
		
		int rowsDeletedFolder = -1;
		MailBean emailFound = new MailBean();
		ArrayList<String> emailAddressFound = new ArrayList<>();
		ArrayList<EmailAttachment> attachmentFound = new ArrayList<>();
		
		try {
			md.createMail(mailBean);
			rowsDeletedFolder = md.deleteFolder(mailBean.getFolder());
			emailFound = md.findMailById(mailBean.getId());
			emailAddressFound = md.findEmailAddressByEmailId(mailBean.getId());
			attachmentFound = md.findAttachmentByEmailId(mailBean.getId());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("Does not work", 1, rowsDeletedFolder);
		assertEquals("Does not work", new MailBean(), emailFound);
		assertEquals("Does not work", 0, emailAddressFound.size());
		assertEquals("Does not work", 0, attachmentFound.size());
	}
	
	/**
	 * This method deletes a row in mail table
	 */
	
	@Test
	public void deleteSimpleMail() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		mailBean.setFolder("TEST");
		
		int rowsDeleted = -1;
		
		try {
			//Create folder
			md.createMail(mailBean);
			
			
			rowsDeleted = md.deleteMail(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have deleted 1 rows
		assertEquals("Mail not deleted", 1, rowsDeleted);
	} 

	/**
	 * This method tries to delete mail that does not exist
	 */
	
	@Test
	public void tryToDeleteMailThatDoesNotExist() {
		MailDAOImpl md = new MailDAOImpl();
		
		int rowsDeleted = -1;
		
		try {
			int randomMailId = 2530;
			rowsDeleted = md.deleteMail(randomMailId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have deleted 0 rows
		assertEquals("Mail not deleted", 0, rowsDeleted);
	}
	
	/**
	 * This method tries to delete complex mail with attachments
	 */
	
	@Test
	public void deleteComplexMail() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("TEST");
		mailBean.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBean.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("turtle.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("pyramid.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		int rowsDeleted = -1;
		
		try {
			md.createMail(mailBean);
			
			rowsDeleted = md.deleteMail(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to have deleted 1 rows
		assertEquals("Mail not deleted", 1, rowsDeleted);
	}
	
	/**
	 * This method tries to delete a Mail. This test is specifically to check
	 * that on delete cascade works, so it deletes the attachments and the addresses in their respective tables
	 */
	@Test
	public void tryDeletingMailCascade() {
		MailDAOImpl md = new MailDAOImpl(); 
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("TEST");
		mailBean.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBean.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("turtle.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("pyramid.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		mailBean.setFolder("TEST");
		
		int rowsDeletedMail = -1;
		ArrayList<String> emailAddressFound = new ArrayList<>();
		ArrayList<EmailAttachment> attachmentFound = new ArrayList<>();
		
		try {
			md.createMail(mailBean);
			rowsDeletedMail = md.deleteMail(mailBean.getId());
			emailAddressFound = md.findEmailAddressByEmailId(mailBean.getId());
			attachmentFound = md.findAttachmentByEmailId(mailBean.getId());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("Does not work", 1, rowsDeletedMail);
		assertEquals("Does not work", 0, emailAddressFound.size());
		assertEquals("Does not work", 0, attachmentFound.size());
	}
	
	/**
	 * This tests finds mail by an id
	 */
	
	@Test
	public void testFindMailById() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean foundMB = new MailBean();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("TEST");
		mailBean.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBean.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("turtle.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("pyramid.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		try {
			md.createMail(mailBean);
			
			foundMB = md.findMailById(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, foundMB);
	}
	
	/**
	 * This method tries to find mail by an id
	 */
	
	@Test
	public void testFindMailByIdDoesNotExist() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean foundMB = new MailBean();
		
		try {
			foundMB = md.findMailById(99999);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//The MailBean id by default is 0 if it does not exist
		assertEquals("Did not find mail", 0, foundMB.getId());
	}
	
	/**
	 * This method retrieves all the folder names
	 */
	
	@Test
	public void testFindAllFolder() {
		MailDAOImpl md = new MailDAOImpl();
		ArrayList<String> folders = new ArrayList<>();
		try {
			md.createFolder("TestFolder1");
			md.createFolder("TestFolder2");
			md.createFolder("TestFolder3");
			md.createFolder("TestFolder4");
			md.createFolder("TestFolder4"); //Duplicate so won't be created
			
			folders = md.findAllFolderNames();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Supposed to send back an array list with a size of 4
		assertEquals("Did not find mail", 4, folders.size());
	}
	
	/**
	 * Tests find mail by Subject
	 */
	
	@Test
	public void testFindMailBySubject() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		try {
			md.createMail(mailBean);
			mailList = md.findMailBySubject(mailBean.getSubjectField());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, mailList.get(0));
	}
	
	/**
	 * Tests find mail by From field
	 */
	
	@Test
	public void testFindMailByFromField() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		try {
			md.createMail(mailBean);
			mailList = md.findMailByFromField(mailBean.getFromField());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, mailList.get(0));
	}
	
	/**
	 * Tests find mail by date sent
	 */
	
	@Test
	public void testFindMailByDateSent() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		//MailBean testBean = new MailBean();
		try {
			md.createMail(mailBean);
			mailList = md.findMailByDateSent(mailBean.getDateSent());
			//testBean = md.findMailById(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//log.debug((Timestamp.valueOf(mailBean.getDateSent())).toString());
		//log.debug((Timestamp.valueOf(testBean.getDateSent())).toString());
		
		assertEquals("MailBeans are not the same", mailBean, mailList.get(0));
	}
	
	/**
	 * Tests find mail by date received
	 */
	
	@Test
	public void testFindMailByDateReceived() {
		MailDAOImpl md = new MailDAOImpl();
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		//MailBean testBean = new MailBean();
		try {
			md.createMail(mailBean);
			mailList = md.findMailByDateReceived(mailBean.getDateReceived());
			//testBean = md.findMailById(mailBean.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//log.debug((Timestamp.valueOf(mailBean.getDateSent())).toString());
		//log.debug((Timestamp.valueOf(testBean.getDateSent())).toString());
		
		assertEquals("MailBeans are not the same", mailBean, mailList.get(0));
	}
	
	/**
	 * Tests find mail by to field, 1 result
	 */
	
	@Test
	public void testFindMailByToField() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			md.createMail(mailBean);
			mailList = md.findMailByToField(mailBean.getToField().get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, mailList.get(0));
	}
	
	/**
	 * Tests find mail by to field, finds multiple mails 
	 */
	
	@Test
	public void testFindMailByToFieldMultipleResults() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			md.createMail(mailBean);
			md.createMail(mailBean);
			mailList = md.findMailByToField(mailBean.getToField().get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to find 2 MailBeans with the given to field
		assertEquals("Did not find mail", 2, mailList.size());
	}
	
	/**
	 * Tests find mail by to field, no results
	 */
	
	@Test
	public void testFindMailByToFieldNoResult() {
		MailDAOImpl md = new MailDAOImpl();
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			mailList = md.findMailByToField("non.existant.email@gmail.com");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to find 0 MailBeans with the given to field
		assertEquals("Did not find mail",  0, mailList.size());
	}
	
	/**
	 * Tests find mail by cc field, 1 result
	 */
	
	@Test
	public void testFindMailByCCField() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			md.createMail(mailBean);
			mailList = md.findMailByCCField(mailBean.getCCField().get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, mailList.get(0));
	}
	
	/**
	 * Tests find mail by cc field, finds multiple mails 
	 */
	
	@Test
	public void testFindMailByCCFieldMultipleResults() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			md.createMail(mailBean);
			md.createMail(mailBean);
			mailList = md.findMailByCCField(mailBean.getCCField().get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to find 2 MailBeans with the given CC field
		assertEquals("Did not find mail",  2, mailList.size());
	}
	
	/**
	 * Tests find mail by CC field, no results
	 */
	
	@Test
	public void testFindMailByCCFieldNoResult() {
		MailDAOImpl md = new MailDAOImpl();
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			mailList = md.findMailByCCField("non.existant.email@gmail.com");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to find 0 MailBeans with the given CC field
		assertEquals("Did not find mail",  0, mailList.size());
	}

	/**
	 * Tests find mail by bcc field, 1 result
	 */
	
	@Test
	public void testFindMailByBCCField() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			md.createMail(mailBean);
			mailList = md.findMailByBCCField(mailBean.getBCCField().get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, mailList.get(0));
	}
	
	/**
	 * Tests find mail by bcc field, finds multiple mails 
	 */
	
	@Test
	public void testFindMailByBCCFieldMultipleResults() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			md.createMail(mailBean);
			md.createMail(mailBean);
			mailList = md.findMailByBCCField(mailBean.getBCCField().get(0));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to find 2 MailBeans with the given BCC field
		assertEquals("Did not find mail", 2, mailList.size());
	}
	
	/**
	 * Tests find mail by BCC field, no results
	 */
	
	@Test
	public void testFindMailByBCCFieldNoResult() {
		MailDAOImpl md = new MailDAOImpl();
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			mailList = md.findMailByBCCField("non.existant.email@gmail.com");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to find 0 MailBeans with the given BCC field
		assertEquals("Did not find mail", 0, mailList.size());
	}
	
	/**
	 * Tests find mail by folder name, 1 result
	 */
	
	@Test
	public void testFindMailByFolderName() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		mailBean.setFolder("TEST_FOLDER");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			md.createMail(mailBean);
			mailList = md.findMailByFolderName(mailBean.getFolder());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("MailBeans are not the same", mailBean, mailList.get(0));
	}
	
	/**
	 * Tests find mail by folder name, multiple results
	 */
	
	@Test
	public void testFindMailByFolderNameMultipleResults() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		mailBean.setFolder("TEST_FOLDER");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			md.createMail(mailBean);
			md.createMail(mailBean);
			md.createMail(mailBean);
			mailList = md.findMailByFolderName(mailBean.getFolder());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects 3 mails with given folder name
		assertEquals("Did not find mail",  3, mailList.size());
	}
	
	/**
	 * Tests find mail by folder name, zero results
	 */
	
	@Test
	public void testFindMailByFolderNameNoResults() {

		MailDAOImpl md = new MailDAOImpl();
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			mailList = md.findMailByFolderName("RANDOM_FOLDERNAME");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects 0 mails with given folder name
		assertEquals(0, mailList.size());
	}
	
	/**
	 * Tests update folder name
	 */
	
	@Test
	public void testUpdateFolderName() {
		MailDAOImpl md = new MailDAOImpl();				
		int rowsUpdated = 0;
		
		try {
			md.createFolder("TEST");
			rowsUpdated = md.update("TEST", "NEW_FOLDER_NAME");
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expected to update 1 row
		assertEquals(1, rowsUpdated);
	}

	/**
	 * Tests update folder name, no updates
	 */
	
	@Test
	public void testUpdateFolderNameNoChanges() {
		MailDAOImpl md = new MailDAOImpl();				
		int rowsUpdated = 0;
		
		try {
			rowsUpdated = md.update("RANDOM_NON_EXISTANT_FOLDER_NAME", "NEW_FOLDER_NAME");
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expected to update 0 row
		assertEquals("Did not update", 0, rowsUpdated);
	}
	
	/**
	 * Tests update folder name for a mailbean
	 */
	
	@Test
	public void testUpdateFolderNameForMailBean() {
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		mailBean.setFolder("TEST_FOLDER");
		
		int rowsUpdated = 0;
		
		try {
			md.createMail(mailBean);
			
			rowsUpdated = md.update(mailBean, mailBean.getFolder());		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expected to update 1 row
		assertEquals("Did not update", 1, rowsUpdated);
	}
	
	/**
	 * This tests find email address by id
	 * When creating addresses in tables it  
	 * adds the types of email address in this order:
	 * TO, CC, BCC 
	 */
	
	@Test
	public void testFindEmailAddressById() {
		MailDAOImpl md = new MailDAOImpl();
		
		String to = "";
		String to2 = "";
		String cc = "";
		String bcc = "";
		
		//Has 2 To and a CC, no BCC
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.getToField().add("randomEmail");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		try {
			md.createMail(mailBean);
			
			to = md.findEmailAddressById(1);
			to2 = md.findEmailAddressById(2);
			cc = md.findEmailAddressById(3);
			bcc = md.findEmailAddressById(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("Does not work", mailBean.getToField().get(0), to);
		assertEquals("Does not work", mailBean.getToField().get(1), to2);
		assertEquals("Does not work", mailBean.getCCField().get(0), cc);
		assertEquals("Does not work", "", bcc);
	}
	
	/**
	 * This tests find attachment by id
	 * Adds attachment in this order: ATT then EMB
	 */
	
	@Test
	public void testFindAttachmentById(){
		MailDAOImpl md = new MailDAOImpl();
		
		EmailAttachment attachment = null;
		EmailAttachment embedded = null;
		
		//MailBean has 1 attach and 1 embed
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("TEST");
		mailBean.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBean.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		ea = eab.create();
		mailBean.getAttachField().add(ea);
		
		try {
			md.createMail(mailBean);
			
			attachment = md.findAttachmentById(1);
			embedded = md.findAttachmentById(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//FOR ATTACH
		boolean valid = false;
		
		if(Arrays.equals(mailBean.getAttachField().get(0).toByteArray(), attachment.toByteArray())){
			valid = true;
		}
		
		assertTrue(valid);
		
		//FOR EMBED
		valid = false;
		
		if(Arrays.equals(mailBean.getEmbedField().get(0).toByteArray(), embedded.toByteArray())){
			valid = true;
		}
		
		assertTrue(valid);
	}
	/**
	 * This tests find folder name by id
	 */
	
	@Test
	public void testFindFolderNameById(){
		MailDAOImpl md = new MailDAOImpl();
		
		String name1 = "";
		String name2 = "";
		String name3 = "";
		
		try {
			md.createFolder("TEST1");
			md.createFolder("TEST2");
			
			name1 = md.findFolderNameById(1);
			name2 = md.findFolderNameById(2);
			name3 = md.findFolderNameById(10);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals("Does not work", "TEST1", name1);
		assertEquals("Does not work", "TEST2", name2);
		assertEquals("Does not work", "", name3);
	}
	
	/**
	 * Method tests finding all mail
	 */
	@Test
	public void testFindAllMail(){
		MailDAOImpl md = new MailDAOImpl();
		
		MailBean mailBean = new MailBean();
		mailBean.getToField().add("cs.balala.receive@gmail.com");
		mailBean.setFromField("cs.balala.send@gmail.com");
		mailBean.getCCField().add("cs.balala.cc@gmail.com");
		mailBean.getBCCField().add("cs.balala.bcc@gmail.com");
		mailBean.setSubjectField("TEST");
		mailBean.setTextMessageField("This is the text of the message");
		
		ObservableList<MailBean> mailList = FXCollections.observableArrayList();
		
		try {
			//Created 4 mails
			md.createMail(mailBean);
			md.createMail(mailBean);
			md.createMail(mailBean);
			md.createMail(mailBean);
			mailList = md.findAllMail();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Expects to find 4 MailBeans
		assertEquals("Did not find mail", 4, mailList.size());
	}
	
	/*--------------------------------------------------------------------------------------------*/
	
	/**
	 *  The following 4 methods have been given to us by Ken to accomplish the task of
	 *  seeding the database.  It creates the database, the tables and also creates mock
	 *  data that will be run before every test.
	 */
	
	@Before
	public void seedDatabase() {
		final String seedDataScript = loadAsString("createMailMySQL.sql");
		try (Connection connection = DriverManager.getConnection(url, user, password);) {
			for (String statement : splitStatements(new StringReader(seedDataScript), ";")) {
				connection.prepareStatement(statement).execute();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed seeding database", e);
		}
		
	}

	/**
	 * The following methods support the seedDatabse method
	 */
	private String loadAsString(final String path) {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
				Scanner scanner = new Scanner(inputStream)) {
			return scanner.useDelimiter("\\A").next();
		} catch (IOException e) {
			throw new RuntimeException("Unable to close input stream.", e);
		}
	}

	private List<String> splitStatements(Reader reader, String statementDelimiter) {
		final BufferedReader bufferedReader = new BufferedReader(reader);
		final StringBuilder sqlStatement = new StringBuilder();
		final List<String> statements = new LinkedList<String>();
		try {
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || isComment(line)) {
					continue;
				}
				sqlStatement.append(line);
				if (line.endsWith(statementDelimiter)) {
					statements.add(sqlStatement.toString());
					sqlStatement.setLength(0);
				}
			}
			return statements;
		} catch (IOException e) {
			throw new RuntimeException("Failed parsing sql", e);
		}
	}

	private boolean isComment(final String line) {
		return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
	}
}
