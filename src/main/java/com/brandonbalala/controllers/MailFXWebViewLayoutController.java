package com.brandonbalala.controllers;

import java.sql.SQLException;
import java.util.ResourceBundle;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAO;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MailFXWebViewLayoutController {

    @FXML
    private WebView mailFXWebView;
    
	// Resource bundle is injected when controller is loaded
    @FXML 
    private ResourceBundle resources;
    
    private MailDAO mailDAO;
	private RootLayoutController rootLayoutController;

    public MailFXWebViewLayoutController(){
    	super();
    }
    
    public void setMailDAO(MailDAO mailDAO) throws SQLException{
    	this.mailDAO = mailDAO;
    }

	public void setWebViewContent(MailBean mailBean) {
		System.out.println("in set web view content mailbean");
		String mailDetails = "<body>"
							+"<p>"+"FROM: " + mailBean.getFromField()+"</p>"
							+"<p>"+"TO: " + mailBean.toFieldProperty().get()+"</p>"
							+"<p>"+"CC: " + mailBean.ccFieldProperty().get()+"</p>"
							+"<p>"+"BCC: " + mailBean.bccFieldProperty().get()+"</p>"
							+"<p>"+"Date sent: " + mailBean.getDateSent()+"</p><hr>"
							+"<h1>"+"Subject: " + mailBean.getSubjectField()+"</h1><hr><br>";
		
		if(mailBean.getHTMLMessageField() == "")
			mailDetails += "<h2>" + mailBean.getHTMLMessageField() + "</h2>";
		else
			mailDetails += "<h2>" + mailBean.getTextMessageField() + "</h2>";
		
		mailDetails += "</body>";
							
		mailFXWebView.getEngine().loadContent(mailDetails);
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded. Not much to do here.
	 */
	@FXML
	private void initialize() {
		
	}

	public void setRootLayout(RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
		
	}
	
}
