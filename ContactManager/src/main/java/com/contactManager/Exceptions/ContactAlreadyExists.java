package com.contactManager.Exceptions;

public class ContactAlreadyExists extends Exception {
	
	private static final long serialVersionUID = 8708039957981348104L;

	public ContactAlreadyExists(){
		super("Contact Already Exists");
	}

}
