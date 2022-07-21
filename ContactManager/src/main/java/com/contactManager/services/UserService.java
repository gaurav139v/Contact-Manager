package com.contactManager.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.contactManager.Exceptions.UserAlreadyRegistered;
import com.contactManager.dao.ContactRepository;
import com.contactManager.dao.UserRepository;

import com.contactManager.entities.User;

@Service
public class UserService {
	
	@Autowired
	private User user;	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;	
	
	public User getUserByUsername(String email) {
		return this.userRepository.getUserByUsername(email);
	}
	
	/*
	 * This will register the user 
	 * Return: User Id 
	 * if fails to register return 0
	 */ 	
	public Integer registerUser(User user) throws UserAlreadyRegistered {		
		
		User existingUser = this.userRepository.getUserByUsername(user.getEmail());
		if(existingUser != null) {
			throw new UserAlreadyRegistered();
		}
			
		// Adding few user details
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("default.png");
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			
		user = this.userRepository.save(user);
		
		return user.getId();
	}

	@Override
	public String toString() {
		return "UserService [user=" + user + ", userRepository=" + userRepository + ", contactRepository="
				+ contactRepository + ", passwordEncoder=" + passwordEncoder + "]";
	}
	
	
}
