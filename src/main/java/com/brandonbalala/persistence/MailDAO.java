package com.brandonbalala.persistence;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.brandonbalala.mailbean.MailBean;

import javafx.collections.ObservableList;
import jodd.mail.EmailAttachment;

/**
 * Interface for CRUD methods
 * 
 * @author Brandon Balala
 */
public interface MailDAO {
	//Create
	public int createMail(MailBean mail) throws SQLException;
	public int createFolder(String folderName) throws SQLException;
	
	//Read (EXACT MATCH not Contains)
	public ObservableList<MailBean> findMailBySubject(String subject) throws SQLException;
	public ObservableList<MailBean> findMailByToField(String email) throws SQLException;
	public ObservableList<MailBean> findMailByFromField(String email) throws SQLException;
	public ObservableList<MailBean> findMailByCCField(String email) throws SQLException;
	public ObservableList<MailBean> findMailByBCCField(String email) throws SQLException;
	public ObservableList<MailBean> findMailByDateSent(LocalDateTime datetime) throws SQLException;
	public ObservableList<MailBean> findMailByDateReceived(LocalDateTime datetime) throws SQLException; 
	public ObservableList<MailBean> findMailByFolderName(String folderName) throws SQLException; 
	public MailBean findMailById(int id) throws SQLException;
	public String findEmailAddressById(int id) throws SQLException;
	public EmailAttachment findAttachmentById(int id) throws SQLException; 
	public String findFolderNameById(int id) throws SQLException;
	public ObservableList<MailBean> findAllFolder() throws SQLException;
	public ObservableList<MailBean> findAllMail () throws SQLException;
	public ArrayList<String> findAllFolderNames() throws SQLException;
	public ObservableList<String> findAllFolderNamesObs() throws SQLException;
	
	//Update
	public int update(String folderName, String newName) throws SQLException;
	public int update(int id, String newFolderName) throws SQLException;
	
	//Delete
	public int deleteFolder(String name) throws SQLException;
	public int deleteMail(int id) throws SQLException;
}
