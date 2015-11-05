package com.brandonbalala.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.brandonbalala.persistence.MailDAO;
import com.brandonbalala.persistence.MailDAOImpl;
import com.brandonbalala.gui.MainAppFX;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
	private MailFXHTMLEditorLayoutController mailFXHTMLEditorLayoutController;
	
	public RootLayoutController() {
		super();
		mailDAO = new MailDAOImpl();
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
			// fishFXHTMLController.displayFishAsHTML();
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
	
	private void initWebView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXWebViewLayout.fxml"));

			AnchorPane webView = (AnchorPane) loader.load();

			// Give the controller the data object.
			mailFXWebViewLayoutController = loader.getController();
			mailFXWebViewLayoutController.setMailDAO(mailDAO);
			editorBorderPane.setCenter(webView);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initEditor() {
		try {
			System.out.println("EDITOR");
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXHTMLEditorLayout.fxml"));
			loader.setResources(ResourceBundle.getBundle("MessagesBundle"));
			
			AnchorPane htmlEditor = (AnchorPane) loader.load();
			
			Scene scene = new Scene(htmlEditor);
			
			Stage dialog = new Stage();
			dialog.initStyle(StageStyle.UTILITY);
			
			dialog.setScene(scene);
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

	public void setComboBoxData() {

		searchComboBox.getItems().clear();

		searchComboBox.getItems().addAll("ID", "Subject", "TO", "CC", "BCC", "Date Sent", "Date Received");
	}
	
    @FXML
    void sendMail(ActionEvent event) {
    	initEditor();
    }

	@FXML
	void searchMail(ActionEvent event) {
		
	}
}
