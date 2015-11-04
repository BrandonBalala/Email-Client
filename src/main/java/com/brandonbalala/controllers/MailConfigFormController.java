package com.brandonbalala.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

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
    
    public MailConfigFormController(){
    	super();
    	mailConfigBean = new MailConfigBean();
    }
    
    @FXML
    void createMailConfig(ActionEvent event) {
    	//MUST DO VALIDATION FOR ALL OF THE PROPERTIES
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
    	mailConfigBean.setDbPassword(dbPasswordTextField.textProperty().get());
    	
    	PropertiesManager pm = new PropertiesManager();
    	
    	try {
			pm.writeTextProperties("src/main/resources", "mailConfig", mailConfigBean);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void exitForm(ActionEvent event) {
    	Platform.exit();
    }
}
