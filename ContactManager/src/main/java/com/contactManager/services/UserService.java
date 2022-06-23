package com.contactManager.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.contactManager.Exceptions.ContactAlreadyExists;
import com.contactManager.Exceptions.UserAlreadyRegistered;
import com.contactManager.dao.ContactRepository;
import com.contactManager.dao.UserRepository;
import com.contactManager.entities.Contact;
import com.contactManager.entities.User;

@Service
public class UserService {
	
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
	
	/*
	 * This will save contact 
	 * Return : User with updated contact
	 * */
//	public User saveContact(User user, Contact contact, MultipartFile profileImage) throws ContactAlreadyExists {
//		
//			
//		Contact contactExist = this.contactRepository.getContactByPhone(contact.getPhone());
//		if(contactExist != null) {
//			throw new ContactAlreadyExists();
//		}
//		
//		try {	
//			contact.setUser(user);
//			user.getContacts().add(contact);
//			
//			if(!profileImage.isEmpty()) {
//				
//				contact.setImageUrl(profileImage.getOriginalFilename());
//				File saveFile = new ClassPathResource("static/img/profile").getFile();			
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + profileImage.getOriginalFilename());			
//				Files.copy(profileImage.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
//				
//			}else {
//				contact.setImageUrl("default.png");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		user = this.userRepository.save(user);
//		return user;
//	}
	
}
