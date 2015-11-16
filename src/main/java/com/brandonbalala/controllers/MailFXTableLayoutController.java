package com.brandonbalala.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAO;
import com.brandonbalala.persistence.MailDAOImpl;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    
	/**
	 * Constructor
	 */
	public MailFXTableLayoutController() {
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
     * Set the web view controller
     * @param mailFXWebViewLayoutController
     */
    public void setMailFXWebViewLayoutController(MailFXWebViewLayoutController mailFXWebViewLayoutController){
    	this.mailFXWebViewLayoutController = mailFXWebViewLayoutController;
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
		mailDataTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showMailDetails(newValue));
		
	}
	
	/**
	 * Show the chosen row from the table on the web view 
	 * @param mailBean
	 */
	private void showMailDetails(MailBean mailBean) {
		System.out.println(mailBean.getToField().get(0));
		mailFXWebViewLayoutController.setWebViewContent(mailBean);
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
}
