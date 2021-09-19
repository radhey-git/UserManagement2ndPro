package com.usermanagement.util;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.usermanagement.exception.SMTPException;
import com.usermanagement.exception.UserManagementAppException;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;
	
	private static Logger logger=LoggerFactory.getLogger(EmailUtils.class);

	public boolean sendEmail(String to, String subject, String body) throws UserManagementAppException {
		boolean isTrue = false;
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		logger.debug("Getting Email Request {}",to,subject,body);
		try {

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setText(body,true);
			mailSender.send(mimeMessageHelper.getMimeMessage());
			isTrue = true;
			logger.debug("Getting Email Response {}",isTrue);
		} catch (Exception e) {
			logger.error("Error Occured in Email Functionality {}", new SMTPException(e.getMessage()),e);
		}
		logger.info("Mail Functionality Exicuted Properly",isTrue);
		return isTrue;
	}
}
