package com.brandonbalala.test.mailaction;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brandonbalala.mailaction.BasicSendAndReceive;
import com.brandonbalala.mailbean.MailBean;
import com.brandonbalala.properties.MailConfigBean;
import com.brandonbalala.test.MethodLogger;

import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * A basic test method that determines if a simple message sent is the same
 * message received
 * 
 * @author Brandon Balala
 *
 */
public class MailActionTest {

	// A Rule is implemented as a class with methods that are associated
	// with the lifecycle of a unit test. These methods run when required.
	// Avoids the need to cut and paste code into every test method.
	@Rule
	public MethodLogger methodLogger = new MethodLogger();

	// Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a a plain text message is created, sent, received and compared.
	 */
	@Test
	public void testSendPlainEmail() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 1: PLAIN TEXT");
		mailBeanSend.setTextMessageField("This is the text of the message");
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);
		
		/*
		boolean test = false;
		
		if(mailBeanSend.equals(mailBeansReceive.get(0))){
            test = true;
        }
        
        if(test == false){
            showLog(mailBeanSend, mailBeansReceive);
        }
        
        assertTrue("Messages are not the same", test);
        */
		
		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message written in HTML format is created, sent, received and compared.
	 */
	@Test
	public void testSendHTMLEmail() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 2: HTML message");
		mailBeanSend.setHTMLMessageField("<h1>HTML test message</h1>");
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message that has an html and plain text message is created, sent, received and compared.
	 */
	@Test
	public void testSendBothTypeEmail() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 3: Both type messages");
		mailBeanSend.setTextMessageField("Plain Text message");
		mailBeanSend.setHTMLMessageField("<h1>HTML message</h1>");
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This is to check that you do in fact receive a message you send to yourself
	 * 
	 * WARNING sometimes this email would be received more than 5 seconds laters, which then causes an error being that the next test that runs
	 * will receive and compare this email. So, I increased the waiting time to 10 seconds.
	 */
	@Test
	public void testEmailSentToSelf() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.send@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 4: Test Send to self");
		mailBeanSend.setHTMLMessageField("<h1>Html message</h1>");
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message  is created, sent, received and compared.  It will be sent to multiple people, so it's to check that
	 * the to field is working properly
	 */
	@Test
	public void testEmailToMultiplePeople() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.getToField().add("cs.balala.cc@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 5: Send to multiple people");
		mailBeanSend.setTextMessageField("Plain Text message");
		mailBeanSend.setHTMLMessageField("<h1>HTML message</h1>");
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This is to check that cc field works
	 */
	@Test
	public void testCCEmail() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.getCCField().add("cs.balala.cc@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 6: Message with CC");
		mailBeanSend.setTextMessageField("Plain Text message");
		mailBeanSend.setHTMLMessageField("<h1>HTML message</h1>");
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This is to check that the BCC field works. In this case, we are not supposed
	 * to get back the bcc field.
	 */
	@Test
	public void testBCCEmail() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.getBCCField().add("cs.balala.cc@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 7: Message with BCC");
		mailBeanSend.setTextMessageField("Plain Text message");
		mailBeanSend.setHTMLMessageField("<h1>HTML message - 06.</h1>");
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		boolean test = false;
		
		//Supposed to be different size
		if (mailBeanSend.getBCCField().size() != mailBeansReceive.get(0).getBCCField().size())
			test = true;
        
        if(test == false){
            showLog(mailBeanSend, mailBeansReceive);
        }

		assertTrue("Messages are not the same", test);
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This message will contain an html field and we will be embedding
	 * an image to it.
	 */
	@Test
	public void testEmbedWithHTML() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 8: Embed with HTML");
		mailBeanSend.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");

		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBeanSend.getEmbedField().add(ea);
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This message will contain a plain text message and we will be embedding an image.
	 */
	@Test
	public void testEmbedWithPlainText() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 9: Embed with plain text");
		mailBeanSend.setTextMessageField("Test embed");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBeanSend.getEmbedField().add(ea);
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This message will contain both plain text and html and will be embedding with an image.
	 */
	@Test
	public void testEmbedWithTextAndHTML() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 10: Embed with both message types");
		mailBeanSend.setTextMessageField("Test embed");
		mailBeanSend.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBeanSend.getEmbedField().add(ea);
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This message will contain both plain text and html and will be attaching an image.
	 */
	@Test
	public void testAttachWithText() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 11: Attach with plain text");
		mailBeanSend.setTextMessageField("Test attach");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.create();
		mailBeanSend.getAttachField().add(ea);
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This message will contain both plain text and html and will be attaching an image.
	 */
	@Test
	public void testAttachWithHTML() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 12: Attach with HTML");
		mailBeanSend.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.create();
		mailBeanSend.getAttachField().add(ea);
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This message will contain both plain text and html and will be attaching an image.
	 */
	@Test
	public void testAttachWithTextAndHTML() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 13: Attach with both message types");
		mailBeanSend.setTextMessageField("Test attach");
		mailBeanSend.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.create();
		mailBeanSend.getAttachField().add(ea);
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This message will contain both plain text and html and will be attaching an image.
	 */
	@Test
	public void testAttachAndEmbed() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 14: Embed and Attach");
		mailBeanSend.setTextMessageField("Test attach");
		mailBeanSend.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBeanSend.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachField().add(ea);
		

		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This message will contain both plain text and html and will be attaching an image.
	 */
	@Test
	public void testMultipleAttachments() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 15: Multiple Attachments");
		mailBeanSend.setTextMessageField("Test attach");
		mailBeanSend.setHTMLMessageField("<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><h2>I'm flying!</h2></body></html>");
		
		EmailAttachmentBuilder eab = EmailAttachment.attachment().bytes(new File("FreeFall.jpg"));
		EmailAttachment ea = eab.setInline("FreeFall.jpg").create();
		mailBeanSend.getEmbedField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("turtle.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("WindsorKen180.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachField().add(ea);
		
		eab = EmailAttachment.attachment().bytes(new File("pyramid.jpg"));
		ea = eab.create();
		mailBeanSend.getAttachField().add(ea);
		

		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	/**
	 * Test method for
	 * {@link com.brandonbalala.mailaction.BasicSendAndReceive#sendEmail(com.brandonbalala.mailbean.MailBean, com.brandonbalala.properties.MailConfigBean)}
	 * .
	 *
	 * In this test a message is created, sent, received and compared. This is to check that cc and bcc field works together
	 */
	@Test
	public void testCCandBCCEmail() {
		MailConfigBean sendConfigBean = createSendConfigBean("smtp.gmail.com", "cs.balala.send@gmail.com", "abc123doremi");
		MailConfigBean receiveConfigBean = createReceiveConfigBean("imap.gmail.com", "cs.balala.receive@gmail.com", "abc123doremi");
		BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

		MailBean mailBeanSend = new MailBean();
		mailBeanSend.getToField().add("cs.balala.receive@gmail.com");
		mailBeanSend.getCCField().add("cs.balala.cc@gmail.com");
		mailBeanSend.getBCCField().add("brandon.yvan1229@gmail.com");
		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());
		mailBeanSend.setSubjectField("TEST 16: Message with CC and BCC");
		mailBeanSend.setTextMessageField("Plain Text message");
		mailBeanSend.setHTMLMessageField("<h1>HTML message</h1>");
		
		basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}
		
		ArrayList<MailBean> mailBeansReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		assertEquals("Messages are not the same", mailBeanSend, mailBeansReceive.get(0));
	}
	
	//SMTP
	private MailConfigBean createSendConfigBean(String host, String userEmailAddress, String password){
		MailConfigBean sendConfigBean = new MailConfigBean();
		sendConfigBean.setSmtp(host);
		sendConfigBean.setUserEmailAddress(userEmailAddress);
		sendConfigBean.setPassword(password);
		
		return sendConfigBean;
	}
	
	//IMAP
	private MailConfigBean createReceiveConfigBean(String host, String userEmailAddress, String password){
		MailConfigBean receiveConfigBean = new MailConfigBean();
		receiveConfigBean.setImap(host);
		receiveConfigBean.setUserEmailAddress(userEmailAddress);
		receiveConfigBean.setPassword(password);
		
		return receiveConfigBean;
	}
	
	//Not used anymore because logging differences in the MailBean equals method
	private void showLog(MailBean mailBeanSend, ArrayList<MailBean> mailBeansReceive){
		log.info("     To: " + mailBeanSend.getToField().get(0) + " : " + mailBeansReceive.get(0).getToField().get(0));
		log.info("   From: " + mailBeanSend.getFromField() + " : " + mailBeansReceive.get(0).getFromField());
		log.info("Subject: " + mailBeanSend.getSubjectField() + " : " + mailBeansReceive.get(0).getSubjectField());
		log.info("   Text: " + mailBeanSend.getTextMessageField() + "=" + mailBeanSend.getTextMessageField().length()
				+ " : " + mailBeansReceive.get(0).getTextMessageField() + "="
				+ mailBeansReceive.get(0).getTextMessageField().length());
		log.info("   HTML: " + mailBeanSend.getHTMLMessageField() + "=" + mailBeanSend.getHTMLMessageField().length()
				+ " : " + mailBeansReceive.get(0).getHTMLMessageField() + "="
				+ mailBeansReceive.get(0).getHTMLMessageField().length());
	}
	
}
