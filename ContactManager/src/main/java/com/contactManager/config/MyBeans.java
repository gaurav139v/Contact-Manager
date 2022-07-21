package com.contactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.contactManager.dao.UserRepository;
import com.contactManager.entities.User;
import com.contactManager.helper.Helper;

@Configuration
public class MyBeans {

	@Bean
	public User getUser() {
		return new User();
	}
	
	@Bean
	public Helper getHelper() {
		return new Helper();
	}
	
	
	
}
