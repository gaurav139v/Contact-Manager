package com.contactManager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactManager.dao.UserRepository;
import com.contactManager.entities.User;

@Controller
public class HomeController {
	
	@Autowired
	UserRepository userRepository;

	@GetMapping("/")
	public String home(Model model) {
		
		model.addAttribute("title", "Home - Contact Manager");
		
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title", "About - Contact Manager");
		
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		
		model.addAttribute("title", "Sign Up - Contact Manager");
		model.addAttribute("user", new User());		
		
		return "signup";
	}
	
	@PostMapping("/do-signup")
	public String registerUser(@ModelAttribute("user") User user, @RequestParam(value = "aggreement", defaultValue="false") boolean aggreement, Model model) {
		
		if(!aggreement) {
			System.out.println("Please aggree Terms and Conditions to proceed!");
		}
		
		user.setRole("ROLE_USER");
		user.setEnabled(true);		
		
		User result = this.userRepository.save(user);
		
		model.addAttribute("user", result);
		System.out.println(user);
		System.out.println(aggreement);
		return "signup";
	}
	
}
