package com.brandonbalala.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.persistence.MailDAO;
import com.brandonbalala.persistence.MailDAOImpl;
import com.brandonbalala.properties.MailConfigBean;
import com.brandonbalala.properties.PropertiesManager;
import com.brandonbalala.gui.MainAppFX;
import com.brandonbalala.mailaction.BasicSendAndReceive;
import com.brandonbalala.mailbean.MailBean;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Class that takes care of the root layout, which containts a menu, toolbar, a
 * layout for a tree, table and even a webview.
 * 
 * @author Brandon Balala
 */
public class RootLayoutController {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	@FXML
	private TextField searchTextField;

	@FXML
	private ComboBox<String> searchComboBox;

	@FXML
	private Button searchButton;

	@FXML
	private BorderPane treeBorderPane;

	@FXML
	private BorderPane tableBorderPane;

	@FXML
	private BorderPane editorBorderPane;

	@FXML
	private ResourceBundle resources;

	@FXML
	private Label footerLabel;

	private MailDAO mailDAO;
	private MailFXTreeLayoutController mailFXTreeLayoutController;
	private MailFXTableLayoutController mailFXTableLayoutController;
	private MailFXWebViewLayoutController mailFXWebViewLayoutController;
	private MainAppFX mainApp;

	private MailConfigBean mailConfigBean;
	private BasicSendAndReceive basicSendAndReceive;

	private Timer timer;

	/**
	 * Constructor
	 */
	public RootLayoutController() {
		super();
		mailDAO = new MailDAOImpl();
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
	 * Here we call upon the methods that load the other containers and then
	 * send the appropriate action command to each container
	 */
	@FXML
	private void initialize() {
		log.info("Initializing the Root Layout");
		initTree();
		initTable();
		initWebView();

		// Tell the tree about the table
		setTableControllerToTree();
		setWebViewControllerToTable();

		try {
			mailFXTreeLayoutController.displayTree();
			mailFXTableLayoutController.displayTheTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		PropertiesManager pm = new PropertiesManager();
		basicSendAndReceive = new BasicSendAndReceive();
		try {
			mailConfigBean = pm.loadTextProperties("", "mailConfig");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		receiveMailEveryTenMins();
	}

	private void receiveMailEveryTenMins() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				log.info("UPDATING WITH NEW MAIL");
				receiveMessages();
			}
		}, 0, 600000);

	}

	/**
	 * Give reference of the web view controller to the table layout
	 */
	private void setWebViewControllerToTable() {
		mailFXTableLayoutController.setMailFXWebViewLayoutController(mailFXWebViewLayoutController);
	}

	/**
	 * Give reference of the table layout to the tree layout
	 */
	private void setTableControllerToTree() {
		mailFXTreeLayoutController.setTableController(mailFXTableLayoutController);
	}

	/**
	 * Setting and configuring the table layout onto the root layout
	 */
	private void initTable() {
		log.info("Initializing the Table Layout");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXTableLayout.fxml"));
			AnchorPane tableView = (AnchorPane) loader.load();

			// Give the controller the data object.
			mailFXTableLayoutController = loader.getController();
			mailFXTableLayoutController.setMailDAO(mailDAO);
			mailFXTableLayoutController.setRootLayout(this);
			tableBorderPane.setCenter(tableView);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setting and configuring the tree layout onto the root layout
	 */
	private void initTree() {
		log.info("Initializing the Tree Layout");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXTreeLayout.fxml"));
			AnchorPane treeView = (AnchorPane) loader.load();

			// Give the controller the data object.
			mailFXTreeLayoutController = loader.getController();
			mailFXTreeLayoutController.setMailDAO(mailDAO);
			mailFXTreeLayoutController.setRootLayout(this);
			treeBorderPane.setCenter(treeView);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setting and configuring the web view layout onto the root layout
	 */
	public void initWebView() {
		try {
			log.info("Initializing the Web View Layout");
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXWebViewLayout.fxml"));

			AnchorPane webView = (AnchorPane) loader.load();

			// Give the controller the data object.
			mailFXWebViewLayoutController = loader.getController();
			mailFXWebViewLayoutController.setMailDAO(mailDAO);
			mailFXWebViewLayoutController.setRootLayout(this);
			editorBorderPane.setCenter(webView);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the item/category that you can search mail by
	 */
	public void setComboBoxData() {

		searchComboBox.getItems().clear();
		searchComboBox.getItems().addAll("Subject", "To", "From", "CC", "BCC", "Folder", "Date Sent", "Date Received");
	}

	/**
	 * Opens up the send mail dialog
	 * 
	 * @param event
	 */
	@FXML
	void sendMail(ActionEvent event) {
		log.info("Showing send mail dialog");
		mainApp.showSendMailDialog();
		try {
			mailFXTreeLayoutController.displayTree();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailFXTableLayoutController.updateTableContent();
	}

	/**
	 * Invoked when user clicks the search button. Methood looks for results
	 * based on choice of filter and the search words
	 * 
	 * @param event
	 */
	@FXML
	void searchMail(ActionEvent event) {
		log.info("Searching");
		if (!searchTextField.getText().equals("")) {
			String search = searchTextField.getText();

			// Get choice
			String choice = (String) searchComboBox.getSelectionModel().getSelectedItem().toString();

			// Instantiate an observable list
			ObservableList<MailBean> mbList = FXCollections.observableArrayList();

			try {
				switch (choice) {
				case "Subject":
					mbList = mailDAO.findMailBySubject(search);
					break;
				case "To":
					mbList = mailDAO.findMailByToField(search);
					break;
				case "From":
					mbList = mailDAO.findMailByFromField(search);
					break;
				case "CC":
					mbList = mailDAO.findMailByCCField(search);
					break;
				case "BCC":
					mbList = mailDAO.findMailByBCCField(search);
					break;
				case "Folder":
					mbList = mailDAO.findMailByFolderName(search);
					break;
				case "Date Sent":
					mbList = mailDAO.findMailBySubject(search);
					break;
				case "Date Received":
					mbList = mailDAO.findMailBySubject(search);
					break;
				}

				// Set items in the tables
				mailFXTableLayoutController.getMailDataTable().setItems(mbList);

				if (mbList.size() > 0)
					setFooterLabelText(mbList.size() + " result(s) found");
				else
					setFooterLabelText("No results found");

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Closing the application
	 * 
	 * @param event
	 */
	@FXML
	void menuClose(ActionEvent event) {
		log.info("Closing");
		timer.cancel();
		Platform.exit();
	}

	/**
	 * Handles when the user clicks on the about button. Displays an alert with
	 * all the information
	 * 
	 * @param event
	 */
	@FXML
	void handleAbout(ActionEvent event) {
		log.info("Showing About alert");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(resources.getString("TITLEABOUT"));
		alert.setHeaderText(resources.getString("HEADERTEXTABOUT"));
		alert.setContentText(resources.getString("CONTEXTABOUT"));

		alert.showAndWait();
	}

	/**
	 * Invoked when user chooses an email in the table and clicks on the delete
	 * button
	 * 
	 * @param event
	 */
	@FXML
	void deleteFolder(ActionEvent event) {
		ObservableList<String> folderNames = FXCollections.observableArrayList();
		try {
			folderNames = mailDAO.findAllFolderNamesObs();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ChoiceDialog<String> dialog = new ChoiceDialog<>("", folderNames);
		dialog.setTitle("Delete Folder");
		dialog.setHeaderText(null);
		dialog.setContentText("Folder to delete:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			// System.out.println("Your choice: " + result.get());
			try {
				mailDAO.deleteFolder(result.get());
				mailFXTreeLayoutController.displayTree();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@FXML
	void createFolder(ActionEvent event) {
		System.out.println("Showing create folder dialog");
		boolean createClicked = mainApp.showCreateFolderDialog();

		if (createClicked) {
			try {
				// Rerfresh the views
				mailFXTreeLayoutController.clearTree();
				mailFXTreeLayoutController.displayTree();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set any text on a label located on the footer of the application
	 * 
	 * @param text
	 */
	public void setFooterLabelText(String text) {
		footerLabel.setText(text);
	}

	/**
	 * Changes the locale to English and refreshes the view
	 * 
	 * @param event
	 */
	@FXML
	void handleChangeEnglish(ActionEvent event) {
		log.info("Changing language to english");
		mainApp.setLocale(new Locale("en", "CA"));
		mainApp.configureRootLayout();
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
		mainApp.configureRootLayout();
	}

	/**
	 * Delete an email or a folder
	 * 
	 * @param event
	 */
	@FXML
	void handleDelete(ActionEvent event) {
		log.info("Changing language to french");
		// TODO
	}

	/**
	 * Fired as an element is being dragged over the delete button area.
	 * 
	 * @param event
	 */
	@FXML
	void handleDragOver(DragEvent event) {
		log.debug("handleDragOver");

		// Accept it only if it is not dragged from the same
		// control and if it has a string data

		if (event.getDragboard().hasString()) {

			// allow for both copying and moving, whatever user
			// chooses
			event.acceptTransferModes(TransferMode.COPY);
		}

		event.consume();
	}

	/**
	 * Fired as an element is being dropped over the delete button area. It
	 * deletes the mail that was dropped.
	 * 
	 * @param event
	 */
	@FXML
	void handleDropped(DragEvent event) {
		log.debug("onDragDropped");

		Dragboard db = event.getDragboard();
		boolean success = false;

		if (db.hasString()) {
			int emailId = Integer.parseInt(db.getString());

			try {
				mailDAO.deleteMail(emailId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			success = true;
		}

		if (success) {
			mailFXTableLayoutController.updateTableContent();
		}

		/*
		 * let the source know whether the string was successfully transferred
		 * and used
		 */
		event.setDropCompleted(true);// success);

		event.consume();
	}

	/**
	 * Set the instance of MailDAO
	 * 
	 * @param mailDAO
	 */
	public void setMailDAO(MailDAO mailDAO) {
		this.mailDAO = mailDAO;
	}

	/**
	 * Receive the new messages and put them in database
	 */
	private void receiveMessages() {
		ArrayList<MailBean> mailBeans = basicSendAndReceive.receiveEmail(mailConfigBean);

		if (mailBeans != null) {
			log.info("Number of mails received: " + mailBeans.size());

			for (MailBean mailbean : mailBeans) {
				try {
					mailDAO.createMail(mailbean);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			mailFXTableLayoutController.updateTableContent();
		}
	}
}
