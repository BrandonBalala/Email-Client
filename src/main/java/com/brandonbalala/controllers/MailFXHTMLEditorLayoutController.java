package com.brandonbalala.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.brandonbalala.mailaction.BasicSendAndReceive;
import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.properties.MailConfigBean;
import com.brandonbalala.properties.PropertiesManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

public class MailFXHTMLEditorLayoutController {

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

	MailConfigBean mailConfigBean;
	BasicSendAndReceive basicSendAndReceive;

	@FXML
	void attachFile(ActionEvent event) {
		/*
		 * FileChooser fileChooser = new FileChooser(); fileChooser.setTitle(
		 * "Choose files to attach"); fileChooser.showOpenDialog(stage);
		 */
	}

	@FXML
	void cancelSending(ActionEvent event) {
		
	}

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
		// ATTACH EMBED STUFF

		if (toList.size() >= 1) {
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

			basicSendAndReceive.sendEmail(mb, mailConfigBean);
		}
	}

	@FXML
	private void initialize() {
		PropertiesManager pm = new PropertiesManager();
		try {
			mailConfigBean = pm.loadTextProperties("src/main/resources", "mailConfig");
			basicSendAndReceive = new BasicSendAndReceive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
