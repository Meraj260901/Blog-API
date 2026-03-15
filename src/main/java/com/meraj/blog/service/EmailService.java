package com.meraj.blog.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	
	public void sendVerificationEmail(String to, String userName, String verificationUrl){
		sendEmailFromTemplate(to, userName, verificationUrl , "Verify your BlogApp account", "verification-email");
	}
	
	public void sendPasswordResetEmail(String to, String userName, String resetUrl){
		sendEmailFromTemplate(to, userName, resetUrl , "Reset your BlogApp password", "password-reset");
	}
	
	private void sendEmailFromTemplate(String to, String userName, String url, String subject, String templateName) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setTo(to);
			helper.setSubject(subject);
			
			Context context = new Context();
			context.setVariable("userName", userName);
			if(templateName.equals("verification-email")) {
				context.setVariable("verificationUrl", url);
			}else {
				context.setVariable("resetUrl", url);
			}
			
			String htmlContent = templateEngine.process(templateName, context);
			
			helper.setText(htmlContent, true);
			mailSender.send(message);			
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send email", e);
		}
	}
}
