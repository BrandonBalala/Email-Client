package com.brandonbalala.mailaction;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import javax.mail.Flags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.properties.MailConfigBean;
import com.brandonbalala.test.MethodLogger;
import com.mysql.jdbc.log.Log;

import jodd.mail.Email;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailFilter;
import jodd.mail.EmailMessage;
import jodd.mail.ImapServer;
import jodd.mail.ImapSslServer;
import jodd.mail.MailAddress;
import jodd.mail.ReceiveMailSession;
import jodd.mail.ReceivedEmail;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;

/**
 * Here is a starter class for phase 1. It sends and receives basic email that
 * consists of a to, from, subject and text.
 * 
 * @author Brandon Balala
 *
 */
public class BasicSendAndReceive {
	
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	/**
	 * This method receives a MailBean and MailConfigBean and takes the necessary
	 * fields to recreate an Email object which will then be sent.
	 * It then returns the messageId of the email it sent.
	 */
	public final String sendEmail(final MailBean mailBean, final MailConfigBean sendConfigBean) {
		// Create am SMTP server object
		SmtpServer<?> smtpServer = SmtpSslServer.create(sendConfigBean.getSmtp())
				.authenticateWith(sendConfigBean.getUserEmailAddress(), sendConfigBean.getPassword());

		Email email = Email.create();
		
		//set fromField
		email.from(sendConfigBean.getUserEmailAddress());
		
		//set toField
		for (String emailAddress : mailBean.getToField()) {
					email.to(emailAddress);
		}
		
		//set ccField
		if (mailBean.getCCField() !=  null && mailBean.getCCField().size() != 0){
			for(String emailAddress : mailBean.getCCField()){
					email.cc(emailAddress);
			}
		}
		
		//set bccField
		if (mailBean.getBCCField() != null && mailBean.getBCCField().size() != 0){
			for(String emailAddress : mailBean.getBCCField()){
					email.bcc(emailAddress);
			}
		}

		//set subjectField
		email.subject(mailBean.getSubjectField());
		
		//set textMessageField
		if(mailBean.getTextMessageField() != null && !mailBean.getTextMessageField().isEmpty())
			email.addText(mailBean.getTextMessageField());
		
		//set htmlMessageField
		if(mailBean.getHTMLMessageField() != null && !mailBean.getHTMLMessageField().isEmpty())
			email.addHtml(mailBean.getHTMLMessageField());		
		
		//set embedField
		if (mailBean.getEmbedField() != null && mailBean.getEmbedField().size() != 0){		
			for(EmailAttachment element : mailBean.getEmbedField()){
				email.embed(element);
			}
		}
		
		//set attachField
		if (mailBean.getAttachField() != null && mailBean.getAttachField().size() != 0){
			for(EmailAttachment element : mailBean.getAttachField()){
				email.attach(element);
			}
		}

		// A session is the object responsible for communicating with the server
		SendMailSession session = smtpServer.createSession();

		//Open the session, send the message and close the session
		session.open();
		String messageId = session.sendMail(email);
		session.close();

		return messageId;
	}

	/**
	 * Method that takes the necessary fields that reconstitutes an Email object 
	 * Returns an array list because there could be more than one message.
	 */
	public final ArrayList<MailBean> receiveEmail(final MailConfigBean receiveConfigBean) {

		ArrayList<MailBean> mailBeans = null;

		// Create an IMAP server that does not display debug info
		
		ImapServer imapServer = new ImapSslServer(receiveConfigBean.getImap(), receiveConfigBean.getUserEmailAddress(),
				receiveConfigBean.getPassword());

		// A session is the object responsible for communicating with the server
		ReceiveMailSession session = imapServer.createSession();
		session.open();

		// We only want messages that have not been read yet.
		// Messages that are delivered are then marked as read on the server
		ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(EmailFilter.filter().flag(Flags.Flag.SEEN, false));

		// If there is any email then loop through them adding their contents to
		// a new MailBean that is then added to the array list.
		if (emails != null) {

			// Instantiate the array list of messages
			mailBeans = new ArrayList<MailBean>();

			for (ReceivedEmail email : emails) {
				
				MailBean mailBean = new MailBean();
				
				//Set fromField
				mailBean.setFromField(email.getFrom().getEmail());
				
				//Set subjectField
				mailBean.setSubjectField(email.getSubject());
				
				//Set toField
				for (MailAddress mailAddress : email.getTo()) {
					mailBean.getToField().add(mailAddress.getEmail());
				}
				
				//Set ccField
				if (email.getCc() != null && email.getCc().length != 0){
					for (MailAddress mailAddress : email.getCc()){
						mailBean.getCCField().add(mailAddress.getEmail());
					}
				}
				
				//Get all the types of messages from email received
				List<EmailMessage> messages = email.getAllMessages();
				int numMsgs;

				//Get first two messages only.
				if(messages.size() < 3){
					numMsgs = messages.size();
				}
				else{
					numMsgs = 2;
				}
					
				for(int cntr = 0; cntr < numMsgs; cntr++){
					//If it's html
					if(messages.get(cntr).getMimeType().equals("TEXT/HTML")){
						mailBean.setHTMLMessageField(messages.get(cntr).getContent());
					}
					//If it's plain text
					else if(messages.get(cntr).getMimeType().equals("TEXT/PLAIN")){
						mailBean.setTextMessageField(messages.get(cntr).getContent());
					}	
				}
				
				//Set the attachField and embedField
				List<EmailAttachment> attachments = email.getAttachments();
				if(attachments != null){
		            for (EmailAttachment attachment : attachments) {
		            	if(attachment.getContentId() == null){
		                    mailBean.getAttachField().add(attachment);
		            	}
		            	else{
		            		mailBean.getEmbedField().add(attachment);
		            	}
		            }
				}
	            
				//Set mail status to seen
				mailBean.setMailStatus(1);
				
				//Set date sent
				mailBean.setDateSent(LocalDateTime.ofInstant((email.getSentDate()).toInstant(), ZoneId.systemDefault()));
				
				//Set date received
				mailBean.setDateReceived(LocalDateTime.ofInstant((email.getReceiveDate()).toInstant(), ZoneId.systemDefault()));
				
				// Add the mailBean to the array list
				mailBeans.add(mailBean);
			}
		}
		session.close();

		return mailBeans;
	}
}
