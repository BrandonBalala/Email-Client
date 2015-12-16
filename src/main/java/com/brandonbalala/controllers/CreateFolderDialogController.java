package com.brandonbalala.controllers;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.persistence.MailDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Opens a simple dialog requesting a user to enter a folder name to create.
 * 
 * @author Brandon Balala
 *
 */
public class CreateFolderDialogController {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	@FXML
	private TextField folderNameTextField;

	private MailDAO mailDAO;
	private Stage dialogStage;
	private boolean createClicked = false;

	/**
	 * Invoked when user clicks on the cancel button. Closes the dialog and
	 * returns focus to primary stage
	 * 
	 * @param event
	 */
	@FXML
	void handleCancel(ActionEvent event) {
		dialogStage.close();
	}

	/**
	 * Invoked when user clicks on the create button. Attempts to creates the
	 * folder in the database and then Closes the dialog and returns focus to
	 * primary stage
	 * 
	 * @param event
	 */
	@FXML
	void handleCreate(ActionEvent event) {
		if (!folderNameTextField.getText().isEmpty()) {
			try {
				mailDAO.createFolder(folderNameTextField.getText().trim());
				createClicked = true;
				dialogStage.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setMailDAO(MailDAO mailDAO) throws SQLException {
		this.mailDAO = mailDAO;
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Returns true whether created button was clicked, otherwise false
	 * 
	 * @return createClicked
	 */
	public boolean isCreateClicked() {
		return createClicked;
	}

}
