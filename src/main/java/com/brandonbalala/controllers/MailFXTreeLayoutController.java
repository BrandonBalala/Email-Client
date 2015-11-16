package com.brandonbalala.controllers;

import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.persistence.MailDAO;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Manipulates the tree related actions. It displays all the folders in the
 * following tree view.  Once these items clicked, they will be displayed onto the table view
 * 
 * @author Brandon
 *
 */
public class MailFXTreeLayoutController {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	private MailDAO mailDAO;
	private MailFXTableLayoutController mailFXTableLayoutController;

	@FXML
	private TreeView<MailBean> mailFXTreeView;

	@FXML
	private ResourceBundle resources;
	private RootLayoutController rootLayoutController;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public MailFXTreeLayoutController() {
		super();
	}

	/**
	 * Set the instance of MailDAO
	 * 
	 * @param mailDAO
	 * @throws SQLException
	 */
	public void setMailDAO(MailDAO mailDAO) throws SQLException {
		this.mailDAO = mailDAO;
	}

	/**
	 * Set the table controller
	 * 
	 * @param mailFXTableLayoutController
	 */
	public void setTableController(MailFXTableLayoutController mailFXTableLayoutController) {
		this.mailFXTableLayoutController = mailFXTableLayoutController;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// We need a root node for the tree and it must be the same type as all
		// nodes
		MailBean rootMailBean = new MailBean();

		// The tree will display common name so we set this for the root
		rootMailBean.setFolder("Folders");// resources.getString("Folders"));//("Folders");
		mailFXTreeView.setRoot(new TreeItem<MailBean>(rootMailBean));

		// This cell factory is used to choose which field in the FihDta object
		// is used for the node name
		mailFXTreeView.setCellFactory((e) -> new TreeCell<MailBean>() {
			@Override
			protected void updateItem(MailBean item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getFolder());
					setGraphic(getTreeItem().getGraphic());
				} else {
					setText("");
					setGraphic(null);
				}
			}
		});
	}

	/**
	 * Build the tree from the database
	 * 
	 * @throws SQLException
	 */
	public void displayTree() throws SQLException {

		// Retreive the list of fish
		ObservableList<MailBean> folderNames = mailDAO.findAllFolder();

		// Build an item for each fish and add it to the root
		if (folderNames != null) {
			for (MailBean fn : folderNames) {
				TreeItem<MailBean> item = new TreeItem<>(fn);
				// item.setGraphic(new
				// ImageView(getClass().getResource("/images/fish.png").toExternalForm()));
				mailFXTreeView.getRoot().getChildren().add(item);
			}
		}

		// Open the tree
		mailFXTreeView.getRoot().setExpanded(true);

		// Listen for selection changes and show the fishData details when
		// changed.
		mailFXTreeView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showMailDetails(newValue));
	}

	/**
	 * Invoked when clicking on an element in the tree. It displays all elements
	 * that have the chosen folder name onto the table layout
	 * 
	 * @param mailBean
	 */
	private void showMailDetails(TreeItem<MailBean> mailBean) {
		// TODO: add link to mail tables!, when user clicks on item in the tree,
		// it shows all emails that have that folder name in the table.
		ObservableList<MailBean> mbList;

		try {
			mbList = mailDAO.findMailByFolderName(mailBean.getValue().getFolder());

			if (mbList.size() > 0)
				rootLayoutController.setFooterLabelText(mbList.size() + " result(s) found");
			else
				rootLayoutController.setFooterLabelText("No results found");

			mailFXTableLayoutController.getMailDataTable().setItems(mbList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Clears everything that is in the tree
	 */
	public void clearTree() {
		// Clear everything in the tree
		mailFXTreeView.getRoot().getChildren().clear();
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