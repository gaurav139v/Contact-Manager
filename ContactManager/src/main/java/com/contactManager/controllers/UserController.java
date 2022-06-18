package com.contactManager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("title", "Dashboard - Contact Manager");		
		return "user/user_dashboard";
	}
}
