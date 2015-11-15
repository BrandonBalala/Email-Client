package com.brandonbalala.gui;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.controllers.MailConfigFormController;
import com.brandonbalala.controllers.MailFXHTMLEditorLayoutController;
import com.brandonbalala.controllers.RootLayoutController;
import com.brandonbalala.persistence.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainAppFX extends Application {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	// primary window or frame of this application
	private Stage primaryStage;
	private Locale currentLocale;
	private MailDAO mailDAO;

	/**
	 * Constructor
	 */
	public MainAppFX() {
		super();
		mailDAO = new MailDAOImpl();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// The Stage comes from the framework so make a copy to use elsewhere
		this.primaryStage = primaryStage;

		//File f = new File("src/main/resources/mailConfig.properties");
		//if (f.exists() && !f.isDirectory()) {
		//	// If found display the app
		//	System.out.println("FOUND THE PROPERTIES");
		//	configureRootLayout();
		//} else {
		//	// If properties not found display mail configuration form
		//	System.out.println("DID NOT FIND THE PROPERTIES");
		//	// Create the Scene and put it on the Stage
			configureMailConfigForm();

		//}
	}

	public void configureRootLayout() {
		this.primaryStage.setTitle(ResourceBundle.getBundle("MessagesBundle").getString("TITLEMAINAPP"));
		Locale locale = Locale.getDefault();
		log.debug("Locale = " + locale);
		currentLocale = new Locale("en", "CA");
		// currentLocale = new Locale("fr","CA");

		// Locale currentLocale = Locale.CANADA;
		// Locale currentLocale = Locale.CANADA_FRENCH;

		try {
			// Instantiate the FXMLLoader
			FXMLLoader loader = new FXMLLoader();

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/RootLayout.fxml"));

			// Localize the loader with its bundle
			// Uses the default locale and if a matching bundle is not found
			// will then use MessagesBundle.properties
			loader.setResources(ResourceBundle.getBundle("MessagesBundle"));

			// Parent is the base class for all nodes that have children in the
			// scene graph such as AnchorPane and most other containers
			Parent parent = (BorderPane) loader.load();

			// Load the parent into a Scene
			Scene scene = new Scene(parent);

			// Put the Scene on Stage
			primaryStage.setScene(scene);
			

			// Raise the curtain on the Stage
			this.primaryStage.show();
			
			//Set data in combo box
			RootLayoutController controller = loader.getController();
			controller.setComboBoxData();
			controller.setMainApp(this);
			
		} catch (IOException ex) { // | SQLException ex) { // getting resources
									// or files
									// could fail
			log.error(null, ex);
			System.exit(1);
		}

	}

	private void configureMailConfigForm() {
		this.primaryStage.setTitle(ResourceBundle.getBundle("MessagesBundle").getString("TITLEFORM"));
		
		try {
			// Instantiate the FXMLLoader
			FXMLLoader loader = new FXMLLoader();

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailConfigForm.fxml"));

			// Localize the loader with its bundle
			// Uses the default locale and if a matching bundle is not found
			// will then use MessagesBundle.properties
			loader.setResources(ResourceBundle.getBundle("MessagesBundle"));

			// Parent is the base class for all nodes that have children in the
			// scene graph such as AnchorPane and most other containers
			Parent parent = (GridPane) loader.load();

			// Load the parent into a Scene
			Scene scene = new Scene(parent);

			// Put the Scene on Stage
			primaryStage.setScene(scene);

			// Raise the curtain on the Stage
			this.primaryStage.show();
			
			MailConfigFormController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException ex) { // | SQLException ex) { // getting resources
									// or files
									// could fail
			log.error(null, ex);
			System.exit(1);
		}

	}
	
	/**
	 * Opens a dialog to edit details for the specified person. If the user
	 * clicks OK, the changes are saved into the provided person object and true
	 * is returned.
	 * 
	 * @param person
	 *            the person object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */
	public boolean showSendMailDialog() {
		try {
			System.out.println("IN SHOW SEND MAIL DIALOG");
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXHTMLEditorLayout.fxml"));
			loader.setResources(ResourceBundle.getBundle("MessagesBundle"));
			AnchorPane page = (AnchorPane) loader.load();
			
			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle(ResourceBundle.getBundle("MessagesBundle").getString("TITLEEDITOR"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			MailFXHTMLEditorLayoutController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			//controller.setPerson(person);

			// Set the dialog icon.
			//dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/edit.png")));

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isSendClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * The beginning
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
