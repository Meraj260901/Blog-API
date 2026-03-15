package com.meraj.blog.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.meraj.blog.entity.User;
import com.meraj.blog.entity.VerificationToken;
import com.meraj.blog.event.RegistrationCompleteEvent;
import com.meraj.blog.repository.UserRepository;
import com.meraj.blog.repository.VerificationTokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher eventPublisher;
	private final VerificationTokenRepository tokenRepository;
	
	public User registerUser(User user, HttpServletRequest request) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled(false);
		User savedUser = userRepository.save(user);
		
		// Publish registration complete event
		String appUrl = applicationUrl(request);
		eventPublisher.publishEvent(new RegistrationCompleteEvent(savedUser, appUrl));
		
		return savedUser;		
	}
	
	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort();
	}
	
	public String verifyAccount(String token) {
		VerificationToken verificationToken = tokenRepository.findByToken(token)
    			.orElseThrow(() -> new RuntimeException("Invalid verification token"));
    			
    	if(verificationToken.isExpired()) {
    		return "Token expired.Please register again.";
    	}
    	
        User user = verificationToken.getUser();
    	user.setEnabled(true);
        userRepository.save(user);
        
        return "Email verified successfully! You can now log in.";
	}
}
