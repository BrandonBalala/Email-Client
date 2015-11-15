package com.brandonbalala.controllers;

import java.sql.SQLException;

import com.brandonbalala.persistence.MailDAO;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateFolderDialogController {

	@FXML
	private TextField folderNameTextField;

	private MailDAO mailDAO;
	private Stage dialogStage;
	private boolean createClicked = false;

	@FXML
	void handleCancel(ActionEvent event) {
		dialogStage.close();
	}

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
        
        // Set the dialog icon.
        //this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

	public boolean isCreateClicked() {
		return createClicked;
	}

}
