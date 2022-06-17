package com.contactManager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/dashboard")
	public String dashboard() {
		return "user/user_dashboard";
	}
}
