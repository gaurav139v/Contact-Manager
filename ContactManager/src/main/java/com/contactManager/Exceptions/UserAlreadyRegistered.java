package com.contactManager.Exceptions;

public class UserAlreadyRegistered extends Exception {
	
	private static final long serialVersionUID = -5036021372639981363L;

	public UserAlreadyRegistered() {
		super("Email already registered");
	}

	
	
	
}
