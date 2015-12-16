/**
 * 
 */
package com.brandonbalala.mailbean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jodd.mail.EmailAttachment;

/**
 * This is the mail bean
 * 
 * @author Brandon Balala
 *
 */
public class MailBean {
	//Id from database
	private IntegerProperty id;
	
	// The address or addresses that this email is being sent to
	private ArrayList<String> toField;
	
	// Carbon copy
	private ArrayList<String> ccField;

	// Blind Carbon copy
	private ArrayList<String> bccField;
	
	// The sender of the email
	private StringProperty fromField;

	// The subject line of the email
	private StringProperty subjectField;
	
	// Plain text part of the email
	private StringProperty textMessageField;
	
	// HTML part of the email
	private StringProperty htmlMessageField;
	
	// Files to embed to email
	private ArrayList<EmailAttachment> embedField;
	
	// Files to attach to the email
	private ArrayList<EmailAttachment> attachField;
	
	// Name of the folder
	private StringProperty folder;

	// Status 0 = New Email for Sending
	// Status 1 = Received Email
	private IntegerProperty mailStatus;
	
	// Date and time the email was sent
	private ObjectProperty<LocalDateTime> dateSent;
	
	// Date and time the email was received
	private ObjectProperty<LocalDateTime> dateReceived;

	/**
	 * Default constructor for a new mail message waiting to be sent
	 */
	public MailBean() {
		super();
		this.id = new SimpleIntegerProperty(0);
		this.toField = new ArrayList<>(); 
		this.ccField = new ArrayList<>(); 
		this.bccField = new ArrayList<>();
		this.fromField = new SimpleStringProperty(""); 
		this.subjectField = new SimpleStringProperty(""); 
		this.textMessageField = new SimpleStringProperty(""); 
		this.htmlMessageField = new SimpleStringProperty(""); 
		this.embedField = new ArrayList<>(); 
		this.attachField = new ArrayList<>(); 
		this.folder = new SimpleStringProperty("Inbox");
		this.mailStatus = new SimpleIntegerProperty(0);
		this.dateSent = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
		this.dateReceived = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
	}

	/**
	 * Constructor for creating messages from either a form or a database record
	 * 
	 * @param toField
	 * @param ccField
	 * @param bccField
	 * @param fromField
	 * @param subjectField
	 * @param textMessageField
	 * @param htmlMessageField
	 * @param embedField
	 * @param attachField
	 * @param folder
	 * @param mailStatus
	 * @param dateSent
	 * @param dateReceived
	 */
	public MailBean(final ArrayList<String> toField, final ArrayList<String> ccField, final ArrayList<String> bccField, final String fromField, final String subjectField,
			final String textMessageField, final String htmlMessageField, final ArrayList<EmailAttachment> embedField, final ArrayList<EmailAttachment> attachField, final String folder, 
			final int mailStatus, final LocalDateTime dateSent, final LocalDateTime dateReceived) {
		super();
		this.id = new SimpleIntegerProperty(0);
		this.toField = toField;
		this.ccField = ccField;
		this.bccField = bccField;
		this.fromField = new SimpleStringProperty(fromField);
		this.subjectField = new SimpleStringProperty(subjectField);
		this.textMessageField = new SimpleStringProperty(textMessageField);
		this.htmlMessageField = new SimpleStringProperty(htmlMessageField);
		this.embedField = embedField;
		this.attachField = attachField;
		this.folder = new SimpleStringProperty(folder);
		this.mailStatus = new SimpleIntegerProperty(mailStatus);
		this.dateSent = new SimpleObjectProperty<LocalDateTime>(dateSent);
		this.dateReceived = new SimpleObjectProperty<LocalDateTime>(dateReceived);
	}
	
	/**
	 * @return the id
	 */ 
	public final int getId(){
		return id.get();
	}
	
	/**
	 * @param id
	 * 		the id to set
	 */
	public final void setId(final int id){
		this.id.set(id);
	}
	
	public IntegerProperty idProperty(){
		return id;
	}

	/**
	 * @return the fromField
	 */
	public final String getFromField() {
		return fromField.get();
	}

	/**
	 * @param fromField
	 *            the fromField to set
	 */
	public final void setFromField(final String fromField) {
		this.fromField.set(fromField);
	}
	
	public StringProperty fromFieldProperty(){
		return fromField;
	}

	/**
	 * @return the subjectField
	 */
	public final String getSubjectField() {
		return subjectField.get();
	}

	/**
	 * @param subjectField
	 *            the subjectField to set
	 */
	public final void setSubjectField(final String subjectField) {
		this.subjectField.set(subjectField);
	}
	
	public StringProperty subjectFieldProperty(){
		return subjectField;
	}
	
	/**
	 * @return the textMessageField
	 */
	public final String getTextMessageField() {
		return textMessageField.get();
	}

	/**
	 * @param textMessageField
	 *            the textMessageField to set
	 */
	public final void setTextMessageField(final String textMessageField) {
		this.textMessageField.set(textMessageField);
	}
	
	public StringProperty textMessageFieldProperty(){
		return textMessageField;
	}
	
	/**
	 * @return the htmlMessageField
	 */
	public final String getHTMLMessageField() {
		return htmlMessageField.get();
	}

	/**
	 * @param htmlMessageField
	 *            the htmlMessageField to set
	 */
	public final void setHTMLMessageField(final String htmlMessageField) {
		this.htmlMessageField.set(htmlMessageField);
	}
	
	public StringProperty htmlMessageProperty(){
		return htmlMessageField;
	}

	/**
	 * @return the folder
	 */
	public final String getFolder() {
		return folder.get();
	}

	/**
	 * @param folder
	 *            the folder to set
	 */
	public final void setFolder(final String folder) {
		this.folder.set(folder);
	}
	
	public StringProperty folderProperty(){
		return folder;
	}

	/**
	 * @return the mailStatus
	 */
	public final int getMailStatus() {
		return mailStatus.get();
	}

	/**
	 * @param mailStatus
	 *            the mailStatus to set
	 */
	public final void setMailStatus(final int mailStatus) {
		this.mailStatus.set(mailStatus);
	}
	
	public IntegerProperty mailStatusProperty(){
		return mailStatus;
	}
	
	/**
	 * @return the dateSent
	 */
	public final LocalDateTime getDateSent() {
		return dateSent.get();
	}

	/**
	 * @param dateSent
	 *            the dateSent to set
	 */
	public final void setDateSent(final LocalDateTime dateSent) {
		this.dateSent.set(dateSent);
	}
	
	public ObjectProperty<LocalDateTime> dateSentProperty(){
		return dateSent;
	}
	
	/**
	 * @return the dateReceived
	 */
	public final LocalDateTime getDateReceived() {
		return dateReceived.get();
	}

	/**
	 * @param dateReceived
	 *            the dateReceived to set
	 */
	public final void setDateReceived(final LocalDateTime dateReceived) {
		this.dateReceived.set(dateReceived);
	}
	
	public ObjectProperty<LocalDateTime> dateReceivedProperty(){
		return dateReceived;
	}

	/**
	 * 
	 * @return the toField
	 */
	public final ArrayList<String> getToField() {
		return toField;
	}
	
	public StringProperty toFieldProperty(){
		String to = "";
		
		//Creates comma seperated list of the cc emails
		for(int cntr = 0; cntr < toField.size(); cntr++){
			to += toField.get(cntr);
			
			if(cntr != toField.size()-1)
				to += ", ";
		}
			
		return new SimpleStringProperty(to);
	}
	
	/**
	 * 
	 * @return the ccField
	 */
	public final ArrayList<String> getCCField() {
		return ccField;
	}
	
	public StringProperty ccFieldProperty(){
		String cc = "";
		
		//Creates comma seperated list of the cc emails
		for(int cntr = 0; cntr < ccField.size(); cntr++){
			cc += ccField.get(cntr);
			
			if(cntr != ccField.size()-1)
				cc += ", ";
		}
			
		return new SimpleStringProperty(cc);
	}
	
	/**
	 * 
	 * @return the bccField
	 */
	public final ArrayList<String> getBCCField() {
		return bccField;
	}
	
	public StringProperty bccFieldProperty(){
		String bcc = "";
		
		//Creates comma seperated list of the bcc emails
		for(int cntr = 0; cntr < bccField.size(); cntr++){
			bcc += bccField.get(cntr);
			
			if(cntr != bccField.size()-1)
				bcc += ", ";
		}
			
		return new SimpleStringProperty(bcc);
	}
	
	/**
	 * 
	 * @return the embedField
	 */
	public final ArrayList<EmailAttachment> getEmbedField() {
		return embedField;
	}
	
	public StringProperty embedFieldProperty(){
		String embed = "";
		
		//Creates comma seperated list of the the names of the embedded files
		for(int cntr = 0; cntr < embedField.size(); cntr++){
			embed += embedField.get(cntr).getContentId();
			
			if(cntr != embedField.size()-1)
				embed += ", ";
		}
			
		return new SimpleStringProperty(embed);
	}
	
	/**
	 * 
	 * @return the attachField
	 */
	public final ArrayList<EmailAttachment> getAttachField() {
		return attachField;
	}
	
	public StringProperty attachFieldProperty(){
		String attach = "";
		
		//Creates comma seperated list of the the names of the embedded files
		for(int cntr = 0; cntr < attachField.size(); cntr++){
			attach += attachField.get(cntr).getName();
			
			if(cntr != attachField.size()-1)
				attach += ", ";
		}
		
		return new SimpleStringProperty(attach);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachField == null) ? 0 : attachField.hashCode());
		result = prime * result + ((ccField == null) ? 0 : ccField.hashCode());
		result = prime * result + ((embedField == null) ? 0 : embedField.hashCode());
		result = prime * result + ((fromField.get() == null) ? 0 : fromField.hashCode());
		result = prime * result + ((htmlMessageField.get() == null) ? 0 : htmlMessageField.hashCode());
		result = prime * result + ((subjectField.get() == null) ? 0 : subjectField.hashCode());
		result = prime * result + ((textMessageField.get() == null) ? 0 : textMessageField.hashCode());
		result = prime * result + ((toField == null) ? 0 : toField.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Logger log = LoggerFactory.getLogger(getClass().getName());
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		MailBean other = (MailBean) obj;
		
		//To field
		if (toField.size() != other.toField.size()){
			log.debug("toField do not match");
			return false;
		}
		else{
			for(int cntr = 0; cntr < toField.size(); cntr++){
				if(!toField.get(cntr).trim().equals(other.toField.get(cntr).trim())){
					log.debug("toFields do not match");
					return false;
				}
			}
		}
		
		//CC field
		if (ccField.size() != other.ccField.size()){
			log.debug("ccField do not match");
			return false;
		}
		else{
			for(int cntr = 0; cntr < ccField.size(); cntr++){
				if(!ccField.get(cntr).trim().equals(other.ccField.get(cntr).trim())){
					log.debug("ccField do not match");
					return false;
				}
			}
		}
		
		//From field
		if (fromField.get() == null) {
			if (other.fromField.get() != null){
				log.debug("fromField do not match");
				return false;	
			}
		} else if (!fromField.get().trim().equals(other.fromField.get().trim())){
			log.debug("fromField do not match");
			return false;
		}
		
		//Subject field
		if (subjectField.get() == null) {
			if (other.subjectField.get() != null){
				log.debug("subjectField do not match");
				return false;
			}
		} else if (!subjectField.get().trim().equals(other.subjectField.get().trim())){
			log.debug("subjectField do not match");
			return false;
		}
		
		//Text message field
		if (textMessageField.get() == null) {
			if (other.textMessageField.get() != null){
				log.debug("textMessageField do not match");
				return false;
			}
		} else if (!textMessageField.get().trim().equals(other.textMessageField.get().trim())){
			log.debug("textMessageField do not match");
			return false;
		}
		
		//HTML message field
		if (htmlMessageField.get() == null) {
			if (other.htmlMessageField.get() != null){
				log.debug("htmlMessageField do not match");
				return false;
			}
		} else if (!htmlMessageField.get().trim().equals(other.htmlMessageField.get().trim())){
			log.debug("htmlMessageField do not match");
			return false;
		}
		
		//Embed field
		if (embedField.size() != other.embedField.size()){
			log.debug("embedField do not match");
			return false;
		}
		else{
			for(int cntr = 0; cntr < embedField.size(); cntr++){
				if(!Arrays.equals(embedField.get(cntr).toByteArray(), other.embedField.get(cntr).toByteArray())){
					log.debug("embedField do not match");
					return false;
				}
			}
		}
		
		//Attach field
		if (attachField.size() != other.attachField.size()){
			log.debug("attachField do not match");
			return false;
		}
		else{
			for(int cntr = 0; cntr < attachField.size(); cntr++){
				if(!Arrays.equals(attachField.get(cntr).toByteArray(), other.attachField.get(cntr).toByteArray())){
					log.debug("attachField do not match");
					return false;
				}
			}
		}
		
		return true;
	}

	
	
}
