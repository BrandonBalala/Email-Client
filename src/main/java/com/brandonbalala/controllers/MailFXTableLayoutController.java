package com.brandonbalala.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAO;
import com.brandonbalala.persistence.MailDAOImpl;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MailFXTableLayoutController {

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
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public MailFXTableLayoutController() {
		super();
	}
    
    public void setMailDAO(MailDAO mailDAO) throws SQLException{
    	this.mailDAO = mailDAO;
    }
    
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
	
		adjustColumnWidths();
		
		// Listen for selection changes and show the fishData details when
		// changed.
		mailDataTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showMailDetails(newValue));
		
	}
	
	private void showMailDetails(MailBean mailBean) {
		System.out.println(mailBean.getToField().get(0));
		mailFXWebViewLayoutController.setWebViewContent(mailBean);
	}

	private void adjustColumnWidths() {
		// TODO Auto-generated method stub	
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
	 * The table displays the fish data
	 * 
	 * @throws SQLException
	 */
	public void displayTheTable() throws SQLException {
		// Add observable list data to the table
		mailDataTable.setItems(mailDAO.findAllMail());
	}   
}
