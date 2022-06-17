package com.contactManager.controllers;



import javax.servlet.http.HttpSession;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactManager.dao.UserRepository;
import com.contactManager.entities.User;
import com.contactManager.helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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

	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
	@PostMapping("/do-signup")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bResult,
			@RequestParam(value = "aggreement", defaultValue="false") boolean aggreement,
			Model model, HttpSession session
			) {
		
		try {
			if(!aggreement) {
				System.out.println("Please aggree Terms and Conditions to proceed!");
				throw new Exception("Please aggree Terms and Conditions to proceed!");
			}
			
			if(bResult.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			
			User result = this.userRepository.save(user);
			
			model.addAttribute("user", new User());
			System.out.println(user);
			System.out.println(aggreement);
			
			session.setAttribute("message", new Message("Successfully Registered.", "alert-success"));
			session.setAttribute("status", true);
			return "signup";
			
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			user.setEmail("");
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Email address already registered.", "alert-danger"));
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
