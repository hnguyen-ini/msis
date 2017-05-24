package com.msis.core.service.impl;

import java.io.File;
import java.io.StringWriter;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
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
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import com.msis.core.config.CoreConfig;
import com.msis.core.model.Mail;
import com.msis.core.service.MailService;

@Service(value="mailService")
public class MailServiceImpl implements MailService {
	static Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
	private CoreConfig config;
	
	private static JavaMailSenderImpl mailSender;
	private static VelocityEngine engine;
	
	@PostConstruct
	public void init() {
		try {
			log.info("Init Mail Server..");
			String username = config.emailUserName();
			String password = config.emailPassword();
			Properties props = new Properties();
  			props.put("mail.smtp.host", config.emailHost());
  			props.put("mail.smtp.port", config.emailPort());
  			props.put("mail.smtp.ssl.enable", "true");
//  			props.put("mail.smtp.auth", "true");
//  			props.put("mail.smtp.starttls.enable", "true");
  			props.put("mail.smtps.auth", "true");
  	        props.put("mail.smtps.starttls.enable","true");
  			props.put("mail.smtp.timeout", "3000");
  			props.put("mail.smtp.connectiontimeout", "3000");
  			props.put("mail.transport.protocol", "smtps");
		  	Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		  		protected PasswordAuthentication getPasswordAuthentication() {
		  			return new PasswordAuthentication(username, password);
                }
            });
			session.setDebug(false);
			mailSender = new JavaMailSenderImpl();
  			mailSender.setSession(session);
  			mailSender.setHost(config.emailHost());
  			
			engine = new VelocityEngine();
			engine.setProperty("resource.loader", "class");
			engine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		} catch (Exception e) {
			log.warn("Init MailSender Failed, " + e.getMessage());
			e.printStackTrace();
		}
	}
	
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
	  			MimeMessagePreparator preparator = new MimeMessagePreparator() {
	  	            public void prepare(MimeMessage mimeMessage) throws Exception {
	  	                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
	  	                messageHelper.setTo(mail.getAddress());
	  	                messageHelper.setFrom(config.emailNoreply());
	  	                messageHelper.setReplyTo(config.emailReply());
	  	                messageHelper.setSubject(config.registerSubject());
	  	                
	  	                VelocityContext context = new VelocityContext();
	  					context.put("model", mail);
	  					Template template = engine.getTemplate("templates/" + mail.getTemplate(), "UTF-8");
	  					StringWriter writer = new StringWriter();
	  					template.merge(context, writer);
	  	                messageHelper.setText(writer.toString(), true);
	  	                
	  					File file = new File(mail.getName() + "_privatekey.txt"); 
	  					FileUtils.writeStringToFile(file, mail.getPrivateKey(), "UTF-8");
	  					messageHelper.addAttachment(file.getName(), file);
	  	            }
	  	        };
	  			mailSender.send(preparator);
				log.info("Sending DONE!!!");
			} catch (Exception e) {
				log.warn("Seding email failed, " + e.getMessage());
			}
		}
	}
	
}
