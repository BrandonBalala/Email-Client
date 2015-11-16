package com.brandonbalala.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailaction.BasicSendAndReceive;
import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAO;
import com.brandonbalala.properties.MailConfigBean;
import com.brandonbalala.properties.PropertiesManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class MailFXHTMLEditorLayoutController {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	@FXML
	private TextField toTextField;

	@FXML
	private TextField ccTextField;

	@FXML
	private TextField bccTextField;

	@FXML
	private TextField subjectTextField;

	@FXML
	private HTMLEditor htmlEditor;

	@FXML
	private Button attachButton;

	@FXML
	private Button sendButton;

	@FXML
	private Button cancelButton;

	@FXML
	private ResourceBundle resources;

	private MailConfigBean mailConfigBean;
	private BasicSendAndReceive basicSendAndReceive;
	private MailDAO mailDAO;
	private Stage dialogStage;
	private boolean sendClicked = false;

	/**
	 * Constructor
	 */
	public MailFXHTMLEditorLayoutController() {
		super();
	}

	/**
	 * Invoked when user clicks on the attach button. Supposed to be able to
	 * pick files with a file chooser that you want to attach to the email.
	 * 
	 * @param event
	 */
	@FXML
	void attachFile(ActionEvent event) {
		// TODO
		/*
		 * FileChooser fileChooser = new FileChooser(); fileChooser.setTitle(
		 * "Choose files to attach"); fileChooser.showOpenDialog(stage);
		 */
	}

	/**
	 * Invoked when user clicks on the cancel button
	 * 
	 * @param event
	 */
	@FXML
	void cancelSending(ActionEvent event) {
		dialogStage.close();
	}

	/**
	 * Invoked when clicks on the send button. First of all it creates a mail
	 * bean out of what user input. It actually sends it, and also stores the
	 * data in the database.
	 * 
	 * @param event
	 */
	@FXML
	void sendingMail(ActionEvent event) {
		String to = toTextField.getText().trim();
		ArrayList<String> toList = new ArrayList<String>(Arrays.asList(to.split(",")));

		String cc = ccTextField.getText().trim();
		ArrayList<String> ccList = new ArrayList<String>(Arrays.asList(cc.split(",")));

		String bcc = bccTextField.getText().trim();
		ArrayList<String> bccList = new ArrayList<String>(Arrays.asList(bcc.split(",")));

		String subject = subjectTextField.getText().trim();
		String htmlMessage = htmlEditor.getHtmlText().trim();
		// TODO ATTACH/EMBED STUFF

		//Check that to and subject, which is the minimum, are set
		if (toList.size() >= 1 && !subject.isEmpty()) {
			MailBean mb = new MailBean();
			for (String element : toList) {
				mb.getToField().add(element);
			}
			for (String element : ccList) {
				mb.getCCField().add(element);
			}
			for (String element : bccList) {
				mb.getBCCField().add(element);
			}
			mb.setSubjectField(subject);
			mb.setHTMLMessageField(htmlMessage);
			mb.setFromField(mailConfigBean.getUserEmailAddress());
			
			//Sending
			basicSendAndReceive.sendEmail(mb, mailConfigBean);

			try {
				//Storing in database
				mailDAO.createMail(mb);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			sendClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Initializes the property manager and the send and receive class. Also
	 * loads the mail configuration properties from disk
	 */
	@FXML
	private void initialize() {
		PropertiesManager pm = new PropertiesManager();
		basicSendAndReceive = new BasicSendAndReceive();
		try {
			mailConfigBean = pm.loadTextProperties("", "mailConfig");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Set the instance of mailDAO
	 * 
	 * @param mailDAO
	 */
	public void setMailDAO(MailDAO mailDAO) {
		this.mailDAO = mailDAO;
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

		// Set the dialog icon.
		// this.dialogStage.getIcons().add(new
		// Image("file:resources/images/edit.png"));
	}

	/**
	 * Returns true if the user clicked Send button, false otherwise.
	 * 
	 * @return
	 */
	public boolean isSendClicked() {
		return sendClicked;
	}

}
