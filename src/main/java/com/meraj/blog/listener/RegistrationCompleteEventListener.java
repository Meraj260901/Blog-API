package com.meraj.blog.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.meraj.blog.entity.User;
import com.meraj.blog.entity.VerificationToken;
import com.meraj.blog.event.RegistrationCompleteEvent;
import com.meraj.blog.repository.VerificationTokenRepository;
import com.meraj.blog.service.EmailService;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
	
	private final VerificationTokenRepository tokenRepository;
	private final EmailService emailService;
	
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		User user = event.getUser();
		
		// Create verification token
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken(token, user);
		tokenRepository.save(verificationToken);
		
		String verificationUrl = event.getApplicationUrl() + "/api/auth/verify?token=" + token;
		emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationUrl);
	}
		
}
