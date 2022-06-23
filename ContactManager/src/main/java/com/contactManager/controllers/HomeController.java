package com.contactManager.controllers;

import javax.servlet.http.HttpSession;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactManager.Exceptions.UserAlreadyRegistered;

import com.contactManager.entities.User;
import com.contactManager.helper.Message;
import com.contactManager.services.HomeService;
import com.contactManager.services.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private UserService userService;
	
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

	@GetMapping("/signin")
	public String login(Model model) {
		
		model.addAttribute("title", "Login - Contact Manager");
		return "login";
	}
	
	/*
	 * This end point will register the user
	 * 
	 * */	
	@PostMapping("/do-signup")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "aggreement", defaultValue="false") boolean aggreement,
			Model model, HttpSession session
			) {
		
		try {
			
			// Form data validation
			if(!aggreement) {				
				throw new Exception("Please aggree Terms and Conditions to proceed!");
			}
			
			if(result.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			
			// save user
			Integer userId = this.userService.registerUser(user);
					
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered.", "alert-success"));
			session.setAttribute("status", true);
			
			return "signup";
			
		} catch (UserAlreadyRegistered e) {
			// When user is already registered.
			e.printStackTrace();
			user.setEmail("");
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			session.setAttribute("status", false);
			return "signup";
			
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			session.setAttribute("status", false);
			return "signup";			
		} 
	}

	
}
