package com.brandonbalala.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.brandonbalala.persistence.MailDAO;
import com.brandonbalala.persistence.MailDAOImpl;

import com.brandonbalala.gui.MainAppFX;
import com.brandonbalala.mailbean.MailBean;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootLayoutController {
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

	private MailDAO mailDAO;
	private MailFXTreeLayoutController mailFXTreeLayoutController;
	private MailFXTableLayoutController mailFXTableLayoutController;
	private MailFXWebViewLayoutController mailFXWebViewLayoutController;
	private MainAppFX mainApp;

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
		initTree();
		initTable();
		initWebView();
		// initEditorAnchorPane();

		// Tell the tree about the table
		setTableControllerToTree();
		setWebViewControllerToTable();

		try {
			mailFXTreeLayoutController.displayTree();
			mailFXTableLayoutController.displayTheTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setWebViewControllerToTable() {
		mailFXTableLayoutController.setMailFXWebViewLayoutController(mailFXWebViewLayoutController);
	}

	private void setTableControllerToTree() {
		mailFXTreeLayoutController.setTableController(mailFXTableLayoutController);
	}

	private void initTable() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXTableLayout.fxml"));
			AnchorPane tableView = (AnchorPane) loader.load();

			// Give the controller the data object.
			mailFXTableLayoutController = loader.getController();
			mailFXTableLayoutController.setMailDAO(mailDAO);
			tableBorderPane.setCenter(tableView);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	private void initTree() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXTreeLayout.fxml"));
			AnchorPane treeView = (AnchorPane) loader.load();

			// Give the controller the data object.
			mailFXTreeLayoutController = loader.getController();
			mailFXTreeLayoutController.setMailDAO(mailDAO);
			treeBorderPane.setCenter(treeView);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	public void initWebView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

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

	/*
	 * private void initEditor() { try { System.out.println("EDITOR");
	 * FXMLLoader loader = new FXMLLoader(); loader.setResources(resources);
	 * 
	 * loader.setLocation(MainAppFX.class.getResource(
	 * "/fxml/MailFXHTMLEditorLayout.fxml"));
	 * 
	 * AnchorPane htmlEditor = (AnchorPane) loader.load();
	 * 
	 * ScrollPane sp = new ScrollPane(); sp.setContent(htmlEditor);
	 * 
	 * mailFXHTMLEditorLayoutController = loader.getController();
	 * mailFXHTMLEditorLayoutController.setMailDAO(mailDAO);
	 * mailFXHTMLEditorLayoutController.setRootLayout(this);
	 * editorBorderPane.setCenter(sp); } catch (IOException e) {
	 * e.printStackTrace(); } }
	 */

	public void setComboBoxData() {

		searchComboBox.getItems().clear();

		searchComboBox.getItems().addAll("Subject", "To", "From", "CC", "BCC", "Folder", "Date Sent", "Date Received");
	}

	@FXML
	void sendMail(ActionEvent event) {
		System.out.println("SHOW DIALOG");
		mainApp.showSendMailDialog();
	}

	@FXML
	void searchMail(ActionEvent event) {
		if (!searchTextField.getText().equals("")) {
			String search = searchTextField.getText();
			String choice = (String) searchComboBox.getSelectionModel().getSelectedItem().toString();
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
				// case "Date Sent":
				// mbList = mailDAO.findMailBySubject(search);
				// break;
				// case "Date Received":
				// mbList = mailDAO.findMailBySubject(search);
				// break;
				default:
					break;
				}

				mailFXTableLayoutController.getMailDataTable().setItems(mbList);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@FXML
	void menuClose(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void handleAbout(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(resources.getString("TITLEABOUT"));
		alert.setHeaderText(resources.getString("HEADERTEXTABOUT"));
		alert.setContentText(resources.getString("CONTEXTABOUT"));

		alert.showAndWait();
	}
}
