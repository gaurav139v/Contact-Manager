package com.contactManager.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.contactManager.Exceptions.ContactAlreadyExists;
import com.contactManager.Exceptions.NoDataAvailable;
import com.contactManager.Exceptions.UnauthorizedUrl;
import com.contactManager.dao.ContactRepository;
import com.contactManager.dao.UserRepository;
import com.contactManager.entities.Contact;
import com.contactManager.entities.User;
import com.contactManager.helper.Helper;

@Service
public class ContactService {
	
	final private Integer contactsPerPage = 5;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private User user;
	
	@Autowired
	private Helper helper;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

		
	public Page<Contact> getContacts(Integer userId, Integer page) throws NoDataAvailable {
		
		PageRequest pageable = PageRequest.of(page, this.contactsPerPage);		
		Page<Contact> contacts = this.contactRepository.getContactByUserId(userId, pageable);
		
		if(contacts.getTotalPages() == 0) {
			throw new NoDataAvailable();
		}
		
		return contacts;
	}
	
	public Contact getContact(Integer cid) throws UnauthorizedUrl {
		
		Contact contact = this.contactRepository.findById(cid).get();
		
		if (contact.getUser().getId() != this.user.getId()) {
			throw new UnauthorizedUrl();
		}
		
		return contact;
	}
	
	public Contact getContact(String phone) throws UnauthorizedUrl {
		
		Contact contact = this.contactRepository.getContactByPhone(phone, this.user.getId());
			
		return contact;
	}
	
	public boolean deleteContact(Integer cid) throws UnauthorizedUrl {
	
		Contact contact = getContact(cid);
		this.contactRepository.delete(contact);
		if (this.helper.deleteProfileImage(contact.getImageUrl())) {
			return true;
		}
				
		return false;		
	}
	
	public Contact updateContact(Integer cid) throws UnauthorizedUrl {
		
		Contact contact = this.getContact(cid);	
		
		return contact;
	}
	
	public User saveContact(Contact contact, MultipartFile profileImage) throws ContactAlreadyExists, UnauthorizedUrl {
		
		Contact contactExist = this.getContact(contact.getPhone());
		
		if(contactExist != null && contactExist.getCid() != contact.getCid()) {
			throw new ContactAlreadyExists();
		}
		
		try {	
			contact.setUser(this.user);
			this.user.getContacts().add(contact);
			
			if(!profileImage.isEmpty()) {	
				String filename = "IMG-" + String.join("", UUID.randomUUID().toString().split("-"));
				this.helper.saveProfileImage(filename, profileImage);

				contact.setImageUrl(filename);
				
			}else {
				if(contactExist.getImageUrl().isBlank()) {
					contact.setImageUrl("default");
				}
				System.out.println(contact.getImageUrl());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.user = this.userRepository.save(user);
		return this.user;
	}

	@Override
	public String toString() {
		return "ContactService [contactsPerPage=" + contactsPerPage + ", contactRepository=" + contactRepository
				+ ", userRepository=" + userRepository + ", user=" + user + ", helper=" + helper + "]";
	}
	
}
