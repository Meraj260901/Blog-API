package com.meraj.blog.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meraj.blog.entity.PasswordResetToken;
import com.meraj.blog.entity.User;
import com.meraj.blog.entity.VerificationToken;
import com.meraj.blog.payload.AuthRequest;
import com.meraj.blog.payload.AuthResponse;
import com.meraj.blog.repository.PasswordResetTokenRepository;
import com.meraj.blog.repository.UserRepository;
import com.meraj.blog.repository.VerificationTokenRepository;
import com.meraj.blog.security.JwtUtil;
import com.meraj.blog.service.AuthService;
import com.meraj.blog.service.PasswordResetService;
import com.meraj.blog.service.UserLoginService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
    @Autowired 
    private UserRepository userRepository;
    
    @Autowired    
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    @Autowired
    private UserLoginService userLoginService;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user, HttpServletRequest request){    	
    	authService.registerUser(user, request);  	
    	return ResponseEntity.ok("Registration successful! Please check your email to verify your account.");
    }
    
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token) {    	
    	return authService.verifyAccount(token);    	
    }
        
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
    	return userLoginService.login(authRequest);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email, HttpServletRequest request){
    	String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort();
    	passwordResetService.sendPasswordResetLink(email, appUrl);
    	return ResponseEntity.ok("Password reset link sent to your email");
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token,  @RequestParam String newPassword){
    	PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

        return ResponseEntity.ok("Password has been reset successfully");
    }
}
