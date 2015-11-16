package com.brandonbalala.controllers;

import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAO;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Takes care of displaying elements on the webview
 * @author Brandon
 *
 */
public class MailFXWebViewLayoutController {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
    @FXML
    private WebView mailFXWebView;
    
    @FXML 
    private ResourceBundle resources;
    
    private MailDAO mailDAO;
	private RootLayoutController rootLayoutController;

	/**
	 * Constructor
	 */
    public MailFXWebViewLayoutController(){
    	super();
    }
    
	/**
	 * Set the instance of MailDAO
	 * 
	 * @param mailDAO
	 */
    public void setMailDAO(MailDAO mailDAO) throws SQLException{
    	this.mailDAO = mailDAO;
    }

    /**
     * Used to display a mailbean on the web view
     * @param mailBean
     */
	public void setWebViewContent(MailBean mailBean) {
		System.out.println("in set web view content mailbean");
		String mailDetails = "<body>"
							+"<p>"+"From: " + mailBean.getFromField()+"</p>"
							+"<p>"+"To: " + mailBean.toFieldProperty().get()+"</p>"
							+"<p>"+"CC: " + mailBean.ccFieldProperty().get()+"</p>"
							+"<p>"+"Date sent: " + mailBean.getDateSent()+"</p><hr>"
							+"<h3>"+"Subject: " + mailBean.getSubjectField()+"</h3><hr><br>";
		
		if(!mailBean.getHTMLMessageField().isEmpty())
			mailDetails += mailBean.getHTMLMessageField();
		else
			mailDetails += mailBean.getTextMessageField();
		
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

	/**
	 * Set the root layout controller
	 * @param rootLayoutController
	 */
	public void setRootLayout(RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
		
	}
	
}
