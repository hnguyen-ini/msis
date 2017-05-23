package com.msis.core.service.impl;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.msis.common.service.ServiceException;
import com.msis.core.config.CoreConfig;
import com.msis.core.model.Mail;
import com.msis.core.service.MailService;

@Service(value="mailService")
public class MailServiceImpl implements MailService {
	static Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
	private CoreConfig config;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Override
	public void send(Mail mail) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Runnable run = new AsynSending(mail);
		executor.execute(run);
		executor.shutdown();
		
	}

	class AsynSending implements Runnable {
		private Mail mail;
		public AsynSending(Mail mail) {
			this.mail = mail;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(100);
				log.info("Sending mail to " + mail.getAddress());
//				MimeMessage message = mailSender.createMimeMessage();
//				MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
//
//				Template template = velocityEngine.getTemplate("templates/" + mail.getTemplate(), "UTF-8");
//				VelocityContext velocityContext = new VelocityContext();
//				velocityContext.put("model", mail);
//				  
//				StringWriter stringWriter = new StringWriter();
//				template.merge(velocityContext, stringWriter);
//				
//				File file = new File("msisprivatekey.txt"); 
//				FileUtils.writeStringToFile(file, mail.getPrivateKey(), "UTF-8");
//				helper.addAttachment(file.getName(), file);
//				
//				helper.setFrom(config.emailNoreply());
//				helper.setTo(mail.getAddress());
//				helper.setSubject(mail.getSubject());
//  			  	helper.setText(stringWriter.toString());
  			  	//mailSender.send(message);

	  			 MimeMessagePreparator preparator = new MimeMessagePreparator() {
	  	            public void prepare(MimeMessage mimeMessage) throws Exception {
	  	                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
	  	                messageHelper.setTo(mail.getAddress());
	  	                messageHelper.setFrom(config.emailNoreply());
	  	                messageHelper.setReplyTo(config.emailNoreply());
	  	                messageHelper.setSubject(config.registerSubject());
	  	                Map model = new HashMap();
	  	                model.put("model", mail);
	  	                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/" + mail.getTemplate(), model);
	  	                messageHelper.setText(new String(text.getBytes(), "UTF-8"), true);
	  	            }
	  	        };
  			  	
  			  	String username = config.emailUserName();
  			  	String password = config.emailPassword();
  			  	Properties props = new Properties();
	  			props.put("mail.smtp.host", config.emailHost());
	  			props.put("mail.smtp.port", config.emailPort());
	  			props.put("mail.smtps.auth", "true");
	  			props.put("mail.smtp.auth", "true");
	  			props.put("mail.smtp.starttls.enable", "true");
	  			props.put("mail.smtp.ssl.enable", "true");
	  			props.put("mail.transport.protocol", "smtps");
	  	        props.put("mail.smtps.starttls.enable","true");
	  	        
	  			props.put("mail.smtp.timeout", "3000");
	  			props.put("mail.smtp.connectiontimeout", "3000");
  			  	Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	                  protected PasswordAuthentication getPasswordAuthentication() {
	                	  return new PasswordAuthentication(username, password);
	                  }
                });
  			  	session.setDebug(true);
  			  	JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
	  			emailSender.setSession(session);
	  			emailSender.setHost(config.emailHost());
	  			
	  			emailSender.send(preparator);
	  			
				
				log.info("Sending DONE!!!");
			} catch (Exception e) {
				log.warn("Seding email failed, " + e.getMessage());
			}
		}}
	
}
