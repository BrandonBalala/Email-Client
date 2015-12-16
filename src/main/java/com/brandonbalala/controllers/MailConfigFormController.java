package com.brandonbalala.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.gui.MainAppFX;
import com.brandonbalala.properties.*;

/**
 * Takes care of everything about the mail configuration form. This lets you
 * type in all the details concerning the database and mail.
 * 
 * @author Brandon Balala
 *
 */
public class MailConfigFormController {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
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

	@FXML
	private ResourceBundle resources;

	private MailConfigBean mailConfigBean;
	private PropertiesManager pm;
	private MainAppFX mainApp;

	public MailConfigFormController() {
		super();
		mailConfigBean = new MailConfigBean();
	}

	/**
	 * Method invoked as the user clicks the create button, which actually saves
	 * the mail config into a property
	 * 
	 * @param event
	 */
	@FXML
	void createMailConfig(ActionEvent event) {
		log.info("Saving the mail config bean to disk");

		try {
			// Saving the mailConfigBean
			pm.writeTextProperties("", "mailConfig", mailConfigBean);
			mainApp.configureRootLayout();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void initialize() {
		log.info("Initializing the mail config bean in controller");
		pm = new PropertiesManager();

		File file = new File("./mailConfig.properties");
		if (file.exists() && !file.isDirectory()) {
			try {
				// Loading the property file
				mailConfigBean = pm.loadTextProperties("", "mailConfig");
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
		Bindings.bindBidirectional(portTextField.textProperty(), mailConfigBean.portProperty(),
				new NumberStringConverter());
		Bindings.bindBidirectional(dbNameTextField.textProperty(), mailConfigBean.databaseProperty());
		Bindings.bindBidirectional(dbUsernameTextField.textProperty(), mailConfigBean.dbUsernameProperty());
		Bindings.bindBidirectional(dbPasswordTextField.textProperty(), mailConfigBean.dbPasswordProperty());
	}

	/**
	 * Handles exit the form
	 * 
	 * @param event
	 */
	@FXML
	void exitForm(ActionEvent event) {
		log.info("Exit form");
		Platform.exit();
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainAppFX mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Is called when user clicks on the About menu item, which displays an
	 * alert box
	 * 
	 * @param event
	 */
	@FXML
	void handleAbout(ActionEvent event) {
		log.info("About alert box");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(resources.getString("TITLEABOUT"));
		alert.setHeaderText(resources.getString("HEADERTEXTABOUT"));
		alert.setContentText(resources.getString("CONTEXTABOUT"));

		alert.showAndWait();
	}

	/**
	 * Changes the locale to English and refreshes the view
	 * 
	 * @param event
	 */
	@FXML
	void handleChangeEnlglish(ActionEvent event) {
		log.info("Changing language to english");
		mainApp.setLocale(new Locale("en", "CA"));
		mainApp.configureMailConfigForm();
	}

	/**
	 * Changes the locale to French and refreshes the view
	 * 
	 * @param event
	 */
	@FXML
	void handleChangeFrench(ActionEvent event) {
		log.info("Changing language to french");
		mainApp.setLocale(new Locale("fr", "CA"));
		mainApp.configureMailConfigForm();
	}

	/**
	 * Close application as the user clicks on close
	 * 
	 * @param event
	 */
	@FXML
	void handleClose(ActionEvent event) {
		Platform.exit();
	}
}
