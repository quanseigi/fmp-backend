package com.fmp.backend.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmp.backend.jwt.JwtTokenProvider;
import com.fmp.backend.payload.LoginRequest;
import com.fmp.backend.payload.LoginResponse;
import com.fmp.backend.repository.UserRepository;
import com.fmp.backend.model.User;
import com.fmp.backend.model.CustomUserDetails;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public LoginResponse loginOrRegister(@Valid @RequestBody LoginRequest loginRequest) {

		System.out.println(loginRequest.getPassword());
		
		// Register if user does not exist
		if (!userRepository.existsByUsername(loginRequest.getUsername())) {
			User user = new User();
	        user.setUsername(loginRequest.getUsername());
	        user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
	        userRepository.save(user);
		}

		// Login if user exists
		// Check username and password
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		// Return jwt to client
		String jwt = tokenProvider.generateToken((CustomUserDetails) auth.getPrincipal());
		return new LoginResponse(jwt);
	}
}
