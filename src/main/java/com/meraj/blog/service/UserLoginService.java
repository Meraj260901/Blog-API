package com.meraj.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.meraj.blog.entity.User;
import com.meraj.blog.payload.AuthRequest;
import com.meraj.blog.payload.AuthResponse;
import com.meraj.blog.repository.UserRepository;
import com.meraj.blog.security.JwtUtil;

@Service
public class UserLoginService {
	@Autowired
	private AuthenticationManager authenticationManager;
	
    @Autowired 
    private UserRepository userRepository;
     
    @Autowired
    private JwtUtil jwtUtil;   
    
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
			);
			
			User user = userRepository.findByEmail(authRequest.getEmail())
					.orElseThrow(() -> new RuntimeException("User not found"));
			
			if(!user.isEnabled()) {
				return ResponseEntity.status(403).body(new AuthResponse(null, null, "Email not verified. Please verify your account before logging in."));
			}
			
			String token = jwtUtil.generateToken(authRequest.getEmail());    		
			AuthResponse response = new AuthResponse(token,user.getId(), "Login successfull!");
			
			return ResponseEntity.ok(response);
			
		}catch(AuthenticationException e) {
			return ResponseEntity.status(401).body(new AuthResponse(null, null, "Invalid Credentials"));
		}   
    }
}
