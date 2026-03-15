package com.meraj.blog.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.meraj.blog.entity.PasswordResetToken;
import com.meraj.blog.entity.User;
import com.meraj.blog.repository.PasswordResetTokenRepository;
import com.meraj.blog.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
	
	private final UserRepository userRepository;
	private final PasswordResetTokenRepository tokenRepository;
	private final EmailService emailService;
	
	@Transactional
	public void sendPasswordResetLink(String email, String appUrl) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email"));
		
		tokenRepository.deleteByUser(user);
		
		String token = UUID.randomUUID().toString();
		Date expiry = calculateExpiryDate(15);
		PasswordResetToken resetToken = new PasswordResetToken(token, user, expiry);
		tokenRepository.save(resetToken);
		
		String resetUrl = appUrl + "/api/auth/reset-password?token=" + token;
		emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), resetUrl);
	}
	
	private Date calculateExpiryDate(int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
}
