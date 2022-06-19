package com.contactManager.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactManager.dao.UserRepository;
import com.contactManager.entities.Contact;
import com.contactManager.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute
	public void commonAttributes(Model model, Principal principal) {
		
		String username = principal.getName();
		
		User user = this.userRepository.getUserByUsername(username);
		
		model.addAttribute("user", user);	
	}

	@RequestMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		
		model.addAttribute("title", "Dashboard - Contact Manager");		
		
		return "user/user_dashboard";
	}
	
	@GetMapping("/add-contact-form")
	public String addContactForm(Model model) {
		
		model.addAttribute("title", "Dashboard - Contact Manager");
		model.addAttribute("contact", new Contact());
		return "user/contact_form";
	}
	
	@PostMapping("/add-contact")
	public String addContact(@ModelAttribute("contact") Contact contact, @RequestParam("profileImage") MultipartFile file, Model model) {
		try {
			User user = (User) model.getAttribute("user");
			
			// image upload
			if(!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img/profile").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded");
			}
			
			contact.setImageUrl(file.getOriginalFilename());
			contact.setUser(user);
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			
			System.out.println(user);
			
		} catch (Exception e ) {
			System.out.println("Error: " + e.getMessage());
		}
		
		return "user/contact_form";
	}
}
