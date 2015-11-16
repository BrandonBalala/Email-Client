package com.brandonbalala.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.controllers.CreateFolderDialogController;
import com.brandonbalala.controllers.MailConfigFormController;
import com.brandonbalala.controllers.MailFXHTMLEditorLayoutController;
import com.brandonbalala.controllers.RootLayoutController;
import com.brandonbalala.persistence.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * It all starts here. This is the main class which initializes and configures
 * the whole user interface.
 * 
 * In this class, we are able to create the layout of the whole application. It
 * also takes care of opening up dialog boxes, changing the languages and many
 * more.
 * 
 * @author Brandon
 */
public class MainAppFX extends Application {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	// Primary window or frame of this application
	private Stage primaryStage;
	private Locale currentLocale;
	private MailDAO mailDAO;

	/**
	 * Constructor
	 */
	public MainAppFX() {
		super();
		mailDAO = new MailDAOImpl();
		currentLocale = new Locale("en", "CA");
		// currentLocale = new Locale("fr","CA");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// The Stage comes from the framework so make a copy to use elsewhere
		this.primaryStage = primaryStage;

		configureMailConfigForm();
	}

	/**
	 * Loads the layout and the controller for main application, the root
	 * layout. When the RootLayoutController starts, it initializes all of the
	 * other inner layouts that it needs.
	 */
	public void configureRootLayout() {
		log.info("Configuring root layout");
		// Set title
		this.primaryStage.setTitle(ResourceBundle.getBundle("MessagesBundle").getString("TITLEMAINAPP"));

		try {
			// Instantiate the FXMLLoader
			FXMLLoader loader = new FXMLLoader();

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/RootLayout.fxml"));

			// Localize the loader with its bundle
			loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));

			// Parent is the base class for all nodes that have children in the
			// scene graph such as AnchorPane and most other containers
			Parent parent = (BorderPane) loader.load();

			// Load the parent into a Scene
			Scene scene = new Scene(parent);

			// Put the Scene on Stage
			primaryStage.setScene(scene);

			// Raise the curtain on the Stage
			this.primaryStage.show();

			// Set data in combo box
			RootLayoutController controller = loader.getController();
			controller.setComboBoxData();
			controller.setMailDAO(mailDAO);
			controller.setMainApp(this);

		} catch (IOException ex) { // | SQLException ex) { // getting resources
			// or files
			// could fail
			log.error(null, ex);
			System.exit(1);
		}

	}

	/**
	 * Loads the mail config form layout and its controller
	 */
	public void configureMailConfigForm() {
		log.info("Configuring mail config form");
		// Set title
		this.primaryStage.setTitle(ResourceBundle.getBundle("MessagesBundle").getString("TITLEFORM"));

		try {
			// Instantiate the FXMLLoader
			FXMLLoader loader = new FXMLLoader();

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailConfigForm.fxml"));

			// Localize the loader with its bundle
			loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));

			// Parent is the base class for all nodes that have children in the
			// scene graph such as AnchorPane and most other containers
			Parent parent = (BorderPane) loader.load();

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
	 * Opens a dialog that lets you create and send an email
	 * 
	 * @return true if the user clicked send button, false otherwise.
	 */
	public boolean showSendMailDialog() {
		try {
			log.info("Show send mail dialog");
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			
			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXHTMLEditorLayout.fxml"));
			
			// Localize the loader with its bundle
			loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));
			
			AnchorPane dialog = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle(ResourceBundle.getBundle("MessagesBundle").getString("TITLEEDITOR"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(dialog);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			MailFXHTMLEditorLayoutController controller = loader.getController();
			controller.setMailDAO(mailDAO);
			controller.setDialogStage(dialogStage);

			// Set the dialog icon.
			// dialogStage.getIcons().add(new
			// Image(MainApp.class.getResourceAsStream("/images/edit.png")));

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isSendClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Opens a dialog that lets you create a folder
	 * 
	 * @return true if the user clicked Create button, false otherwise.
	 */
	public boolean showCreateFolderDialog() {
		try {
			System.out.println("IN CREATE FOLDER DIALOG");
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			
			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/CreateFolderDialog.fxml"));
			
			// Localize the loader with its bundle
			loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));
			
			GridPane dialog = (GridPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle(ResourceBundle.getBundle("MessagesBundle").getString("TITLEFOLDER"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(dialog);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			CreateFolderDialogController controller = loader.getController();
			controller.setMailDAO(mailDAO);
			controller.setDialogStage(dialogStage);
			// controller.setPerson(person);

			// Set the dialog icon.
			// dialogStage.getIcons().add(new
			// Image(MainApp.class.getResourceAsStream("/images/edit.png")));

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isCreateClicked();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Set the current locale of the application
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.currentLocale = locale;
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
