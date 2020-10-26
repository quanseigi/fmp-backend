package com.fmp.backend.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fmp.backend.model.CustomUserDetails;
import com.fmp.backend.model.User;
import com.fmp.backend.jwt.JwtTokenProvider;
import com.fmp.backend.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	public UserDetails loadUserByUsername(String username) {

		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetails(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

		return new CustomUserDetails(user);
	}
	
	public User getUserFromJwt(String token) {
    	if (token == null)
    		return null;
    	Long userId = tokenProvider.getUserIdFromJWT(token);
    	User user = userRepository.findById(userId).orElse(null);
        return user;
    }
}
