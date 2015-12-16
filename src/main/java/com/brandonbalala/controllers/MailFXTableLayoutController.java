package com.brandonbalala.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAO;
import com.brandonbalala.persistence.MailDAOImpl;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class MailFXTableLayoutController {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	@FXML
	private TableView<MailBean> mailDataTable;

	@FXML
	private TableColumn<MailBean, String> toColumn;

	@FXML
	private TableColumn<MailBean, String> subjectColumn;

	@FXML
	private TableColumn<MailBean, LocalDateTime> dateReceivedColumn;

	private MailFXWebViewLayoutController mailFXWebViewLayoutController;
	private MailDAO mailDAO;
	private RootLayoutController rootLayoutController;
	private String currentFolderNameDisplayed;
	

	/**
	 * Constructor
	 */
	public MailFXTableLayoutController() {
		super();
	}
	
	public String getCurrentFolderNameDisplayed(){
		return currentFolderNameDisplayed;
	}
	
	public void setCurrentFolderNameDisplayed(String folderName){
		this.currentFolderNameDisplayed = folderName;
	}

	/**
	 * Set the instance of MailDAO
	 * 
	 * @param mailDAO
	 */
	public void setMailDAO(MailDAO mailDAO) throws SQLException {
		this.mailDAO = mailDAO;
	}

	/**
	 * Set the web view controller
	 * 
	 * @param mailFXWebViewLayoutController
	 */
	public void setMailFXWebViewLayoutController(MailFXWebViewLayoutController mailFXWebViewLayoutController) {
		this.mailFXWebViewLayoutController = mailFXWebViewLayoutController;
	}
	
	public void updateTableContent(){
		ObservableList<MailBean> mbList;

		try {
			
			mbList = mailDAO.findMailByFolderName(currentFolderNameDisplayed);

			if (mbList.size() > 0)
				rootLayoutController.setFooterLabelText(mbList.size() + " result(s) found");
			else
				rootLayoutController.setFooterLabelText("No results found");

			mailDataTable.setItems(mbList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		toColumn.setCellValueFactory(cellValue -> cellValue.getValue().toFieldProperty());
		subjectColumn.setCellValueFactory(cellValue -> cellValue.getValue().subjectFieldProperty());
		dateReceivedColumn.setCellValueFactory(cellValue -> cellValue.getValue().dateReceivedProperty());

		// Listen for selection changes and show the fishData details when
		// changed.
		mailDataTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showMailDetails(newValue));

		// We are going to drag and drop
		mailDataTable.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				/* drag was detected, start drag-and-drop gesture */
				log.debug("onDragDetected");

				/* allow any transfer mode */
				Dragboard db = mailDataTable.startDragAndDrop(TransferMode.COPY);

				/* put a string on dragboard */
				ClipboardContent content = new ClipboardContent();
				content.putString(String.valueOf(mailDataTable.getSelectionModel().getSelectedItem().getId()));

				db.setContent(content);

				event.consume();
			}
		});
	}

	/**
	 * Show the chosen row from the table on the web view
	 * 
	 * @param mailBean
	 */
	private void showMailDetails(MailBean mailBean) {
		try{
			System.out.println(mailBean.getToField().get(0));
			mailFXWebViewLayoutController.setWebViewContent(mailBean);	
		}
		catch(NullPointerException e){}
	}

	/**
	 * The MailFXTreeController needs a reference to the this controller. With
	 * that reference it can call this method to retrieve a reference to the
	 * TableView and change its selection
	 * 
	 * @return
	 */
	public TableView<MailBean> getMailDataTable() {
		return mailDataTable;
	}

	/**
	 * The table displays all the mails
	 * 
	 * @throws SQLException
	 */
	public void displayTheTable() throws SQLException {
		// Add observable list data to the table
		mailDataTable.setItems(mailDAO.findAllMail());
	}

	@FXML
	private void dragDetected(MouseEvent event) {
		/* drag was detected, start drag-and-drop gesture */
		log.debug("onDragDetected");

		/* allow any transfer mode */
		Dragboard db = mailDataTable.startDragAndDrop(TransferMode.COPY);

		/* put a string on dragboard */
		ClipboardContent content = new ClipboardContent();
		content.putString(String.valueOf(mailDataTable.getSelectionModel().getSelectedItem().getId()));

		db.setContent(content);

		event.consume();
	}
	
	/**
	 * Set the root layout
	 * 
	 * @param rootLayoutController
	 */
	public void setRootLayout(RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
	}
}
