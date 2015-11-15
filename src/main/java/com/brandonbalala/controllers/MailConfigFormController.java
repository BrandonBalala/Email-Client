package com.brandonbalala.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.brandonbalala.gui.MainAppFX;
import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.properties.*;

public class MailConfigFormController {

    @FXML
    private TextField emailAddressTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField smtpTextField;

    @FXML
    private TextField imapTextField;

    @FXML
    private TextField dbURLTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private TextField dbNameTextField;

    @FXML
    private TextField dbUsernameTextField;

    @FXML
    private TextField dbPasswordTextField;

    @FXML
    private TextField fullNameTextField;
    
    private MailConfigBean mailConfigBean;
    private PropertiesManager pm;
    private MainAppFX mainApp;
    
    public MailConfigFormController(){
    	super();
    	mailConfigBean = new MailConfigBean();
    }
    
    @FXML
    void createMailConfig(ActionEvent event) {
    	//MUST DO VALIDATION FOR ALL OF THE PROPERTIES
    	/*
    	mailConfigBean.setUserEmailAddress(emailAddressTextField.textProperty().get());
    	mailConfigBean.setPassword(passwordTextField.textProperty().get());
    	mailConfigBean.setUsername(usernameTextField.textProperty().get());
    	mailConfigBean.setSmtp(smtpTextField.textProperty().get());
    	mailConfigBean.setImap(imapTextField.textProperty().get());
    	mailConfigBean.setFullName(fullNameTextField.textProperty().get());
    	mailConfigBean.setUrl(dbURLTextField.textProperty().get());
    	mailConfigBean.setPort(Integer.parseInt(portTextField.textProperty().get()));
    	mailConfigBean.setDatabase(dbNameTextField.textProperty().get());
    	mailConfigBean.setDbUsername(dbUsernameTextField.textProperty().get());
    	mailConfigBean.setDbPassword(dbPasswordTextField.textProperty().get());*/
    	
    	try {
			pm.writeTextProperties("src/main/resources", "mailConfig", mailConfigBean);
			mainApp.configureRootLayout();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	@FXML
	private void initialize() {
		pm = new PropertiesManager();
		
		File file = new File("src/main/resources/mailConfig.properties");
		if (file.exists() && !file.isDirectory()) {
			try {
				mailConfigBean = pm.loadTextProperties("src/main/resources", "mailConfig");
				
				/*
				emailAddressTextField.setText(mailConfigBean.getUserEmailAddress());
				passwordTextField.setText(mailConfigBean.getPassword());
				usernameTextField.setText(mailConfigBean.getUserEmailAddress());
				smtpTextField.setText(mailConfigBean.getSmtp());
				imapTextField.setText(mailConfigBean.getImap());
				fullNameTextField.setText(mailConfigBean.getFullName());
				dbURLTextField.setText(mailConfigBean.getUrl());
				portTextField.setText(Integer.toString(mailConfigBean.getPort()));
				dbNameTextField.setText(mailConfigBean.getDatabase());
				dbUsernameTextField.setText(mailConfigBean.getDbUsername());
				dbPasswordTextField.setText(mailConfigBean.getDbPassword());*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Bindings.bindBidirectional(emailAddressTextField.textProperty(), mailConfigBean.userEmailAddressProperty());
		Bindings.bindBidirectional(passwordTextField.textProperty(), mailConfigBean.passwordProperty());
		Bindings.bindBidirectional(usernameTextField.textProperty(), mailConfigBean.usernameProperty());
		Bindings.bindBidirectional(smtpTextField.textProperty(), mailConfigBean.smtpProperty());
		Bindings.bindBidirectional(imapTextField.textProperty(), mailConfigBean.imapProperty());
		Bindings.bindBidirectional(fullNameTextField.textProperty(), mailConfigBean.fullNameProperty());
		Bindings.bindBidirectional(dbURLTextField.textProperty(), mailConfigBean.urlProperty());
		Bindings.bindBidirectional(portTextField.textProperty(), mailConfigBean.portProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(dbNameTextField.textProperty(), mailConfigBean.databaseProperty());
		Bindings.bindBidirectional(dbUsernameTextField.textProperty(), mailConfigBean.dbUsernameProperty());
		Bindings.bindBidirectional(dbPasswordTextField.textProperty(), mailConfigBean.dbPasswordProperty());
	}

    @FXML
    void exitForm(ActionEvent event) {
    	Platform.exit();
    }

	public void setMainApp(MainAppFX mainApp) {
		this.mainApp = mainApp;
	}
}
