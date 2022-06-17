package com.contactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contactManager.dao.UserRepository;
import com.contactManager.entities.User;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.getUserByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("User not found!");
		}
		
		CustomUserDetails userDetails = new CustomUserDetails(user);
		
		return userDetails;
	}

}
