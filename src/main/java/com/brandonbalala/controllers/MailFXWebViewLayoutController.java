package com.brandonbalala.controllers;

import java.sql.SQLException;
import java.util.ResourceBundle;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAO;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class MailFXWebViewLayoutController {

    @FXML
    private WebView mailFXWebView;
    
	// Resource bundle is injected when controller is loaded
    @FXML 
    private ResourceBundle resources;
    
    private MailDAO mailDAO;

    public MailFXWebViewLayoutController(){
    	super();
    }
    
    public void setMailDAO(MailDAO mailDAO) throws SQLException{
    	this.mailDAO = mailDAO;
    }

	public void setWebViewContent(MailBean mailBean) {
		String mailDetails = "\nFROM: " + mailBean.getFromField()
							+"\nTO: " + mailBean.toFieldProperty().get()
							+"\nCC: " + mailBean.ccFieldProperty().get()
							+"\nBCC: " + mailBean.bccFieldProperty().get()
							+"\nDate sent: " + mailBean.getDateSent()
							+"\nSubject: " + mailBean.getSubjectField();
		
		if(mailBean.getHTMLMessageField() == "")
			mailDetails += "\n\n" + mailBean.getHTMLMessageField();
		else
			mailDetails += "\n\n" + mailBean.getTextMessageField();
							
		setWebViewContent(mailDetails);
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded. Not much to do here.
	 */
	@FXML
	private void initialize() {
		setWebViewContent("");
	}

	private void setWebViewContent(String string) {
        // create WebView with specified local content
		mailFXWebView.getEngine().load(string);
	}
	
}
