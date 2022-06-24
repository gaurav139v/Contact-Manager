package com.contactManager.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.contactManager.Exceptions.ContactAlreadyExists;
import com.contactManager.Exceptions.NoDataAvailable;
import com.contactManager.Exceptions.UnauthorizedUrl;
import com.contactManager.dao.ContactRepository;
import com.contactManager.dao.UserRepository;
import com.contactManager.entities.Contact;
import com.contactManager.entities.User;
import com.contactManager.helper.Message;
import com.contactManager.services.ContactService;
import com.contactManager.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private static User user = null;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
//	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private ContactService contactService;
	
	@ModelAttribute
	public void commonAttributes(Model model, Principal principal) {
		
		String username = principal.getName();
		User user = this.userService.getUserByUsername(username);
		model.addAttribute("user", user);	
		
		this.userService.setUser(user);
		this.contactService.setUser(user);
		this.user = user;		
	}

	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		
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
	public String addContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
			@RequestParam("profileImage") MultipartFile profileImage, Model model,
			HttpSession session) throws ContactAlreadyExists, UnauthorizedUrl {
		
		// Form data validation
		if(result.hasErrors()) {
			model.addAttribute("contact", contact);
			return "user/contact_form";
		}	
		
		// Save contact
		this.contactService.saveContact(contact, profileImage);
		model.addAttribute("contact", new Contact());
		session.setAttribute("message", new Message("Contact saved", "success"));			
	
		return "user/contact_form";
		
//		// Form data validation
//		if(result.hasErrors()) {
//			model.addAttribute("contact", contact);
//			return "user/contact_form";
//		}
//		
//		try {
//			User user = (User) model.getAttribute("user");				
//			
//			// image upload
//			if(!file.isEmpty()) {
//				File saveFile = new ClassPathResource("static/img/profile").getFile();
//				
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//				
//				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
//				System.out.println("Image is uploaded");
//			}
//			
//			contact.setImageUrl(file.getOriginalFilename());
//			contact.setUser(user);
//			user.getContacts().add(contact);
//			
//			this.userRepository.save(user);
//			model.addAttribute("contact", new Contact());
//			session.setAttribute("message", new Message("Contact saved", "success"));
//			
//			
//		} catch (Exception e ) {
//			e.printStackTrace();
//			session.setAttribute("message", new Message("Something went worng! Try again", "danger"));
//		}
//		
//		return "user/contact_form";
	}
	
	// user controller
	@GetMapping("/view-contacts/{page}")
	public String viewContactPage(@PathVariable("page") Integer page, Model model, Principal principal) throws NoDataAvailable {
		
		Page<Contact> contacts = this.contactService.getContacts(this.user.getId(), page);
		
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		return "user/view_contacts";
		
//		String username = principal.getName();
//		User user = this.userRepository.getUserByUsername(username);
//		
//		Pageable pageable = PageRequest.of(page, 5);
//		
//		Page<Contact> contacts = this.contactRepository.getContactByUserId(user.getId(), pageable);
//		
//		model.addAttribute("contacts", contacts);
//		model.addAttribute("currentPage", page);
//		model.addAttribute("totalPages", contacts.getTotalPages());
//		
//		return "user/view_contacts";
	}
	
	
	@GetMapping("/contact/{cid}")
	public String viewContact(@PathVariable("cid") Integer cid, Model model) throws UnauthorizedUrl {
		
		Contact contact = this.contactService.getContact(cid);
		model.addAttribute("contact", contact);
		return "user/contact";
		
		/*
		 * User user = (User) model.getAttribute("user");
		 * 
		 * Optional<Contact> findById = this.contactRepository.findById(cid); Contact
		 * contact = findById.get();
		 * 
		 * if(user.getId() == contact.getUser().getId()) { model.addAttribute("contact",
		 * contact); }
		 * 
		 * return "user/contact";
		 */
	}
	
	@GetMapping("/delete-contact/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, HttpSession session) throws UnauthorizedUrl {
				
		this.contactService.deleteContact(cid);
		session.setAttribute("message", new Message("Contact deleted!", "success"));
			
		return "redirect:/user/view-contacts/0";
		
//		try {
//			
//			User user = (User) model.getAttribute("user");
//			Contact contact = this.contactRepository.findById(cid).get();
//			
//			if(user.getId() == contact.getUser().getId()) {
//				this.contactRepository.delete(contact);
//				session.setAttribute("message", new Message("Contact deleted!", "success"));
//				
//				// delete profile image
//				File saveFile = new ClassPathResource("static/img/profile").getFile();
//				
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + contact.getImageUrl());
//				
//				Files.delete(path);
//				
//				
//			}else {
//				session.setAttribute("message", new Message("Unauthorized URL!", "danger"));
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return "redirect:/user/view-contacts/0";
		
	}
     	 
	@GetMapping("/update-contact/{cid}")
	public String updateContactPage(@PathVariable("cid") Integer cid, Model model) throws UnauthorizedUrl {
		
		Contact contact = this.contactService.updateContact(cid);
		model.addAttribute("contact", contact);
		
//		User user = (User) model.getAttribute("user");
//		Contact contact = this.contactRepository.findById(cid).get();
//		
//		model.addAttribute("contact", contact);
//		
		return "user/update_contact";
	}
	
	@PostMapping("/update-contact/{cid}/update")
	public String updateContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
			@RequestParam("profileImage") MultipartFile profileImage, Model model,
			HttpSession session) throws ContactAlreadyExists, UnauthorizedUrl {
		
		this.contactService.saveContact(contact, profileImage);
		
		return "redirect:/user/view-contacts/0";
	}
	
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = ContactAlreadyExists.class)
	public String ContactAlreadyExistsException(Model model, HttpSession session) {		
		model.addAttribute("user", this.user);
		model.addAttribute("contact", new Contact());		
		session.setAttribute("message", new Message("Contact Already Exists", "danger"));
		return "user/contact_form";
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = NoDataAvailable.class)
	public String NoDataAvailableException(Model model, HttpSession session) {		
		model.addAttribute("user", this.user);
		model.addAttribute("contacts", new Contact());		
		model.addAttribute("message", new Message("No Data Available", "danger"));
		return "user/view_contacts";
	}
	
	@ResponseStatus(value = HttpStatus.NON_AUTHORITATIVE_INFORMATION)
	@ExceptionHandler(value = UnauthorizedUrl.class)
	public String UnauthorizedUrlException(Model model, HttpSession session) {		
		model.addAttribute("user", this.user);	
		model.addAttribute("message", new Message("Unauthorized Access", "danger"));
		return "user/contact";
	}
	
}
