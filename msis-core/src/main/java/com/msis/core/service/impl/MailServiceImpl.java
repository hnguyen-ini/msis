package com.msis.core.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.msis.core.config.CoreConfig;
import com.msis.core.model.Mail;
import com.msis.core.service.MailService;

@Service(value="mailService")
public class MailServiceImpl implements MailService {
	static Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
	private CoreConfig config;
	
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
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
				MimeMessagePreparator preparator = new MimeMessagePreparator() {

					@Override
					public void prepare(MimeMessage mimeMessage) throws Exception {
						MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
		                messageHelper.setTo(mail.getAddress());
		                messageHelper.setFrom(config.emailNoreply());
		                messageHelper.setReplyTo(config.emailNoreply());
		                messageHelper.setSubject(mail.getSubject());
		                Map model = new HashMap();
		                model.put("model", mail);
		                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, mail.getVelocityModel(), model);
		                messageHelper.setText(new String(text.getBytes(), "UTF-8"), true);
		                log.info("The email message is " + text);          
					}
					
				};
				mailSender.send(preparator);
			} catch (Exception e) {
				log.warn("Seding email failed, " + e.getMessage());
			}
		}}
	
}
