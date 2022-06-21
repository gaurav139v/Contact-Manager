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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactManager.dao.ContactRepository;
import com.contactManager.dao.UserRepository;
import com.contactManager.entities.Contact;
import com.contactManager.entities.User;
import com.contactManager.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void commonAttributes(Model model, Principal principal) {
		
		String username = principal.getName();
		
		User user = this.userRepository.getUserByUsername(username);
		
		model.addAttribute("user", user);	
	}

	@RequestMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		
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
			@RequestParam("profileImage") MultipartFile file, Model model,
			HttpSession session) {
		
		if(result.hasErrors()) {
			model.addAttribute("contact", contact);
			return "user/contact_form";
		}
		
		try {
			User user = (User) model.getAttribute("user");				
			
			// image upload
			if(!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img/profile").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded");
			}
			
			contact.setImageUrl(file.getOriginalFilename());
			contact.setUser(user);
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			model.addAttribute("contact", new Contact());
			session.setAttribute("message", new Message("Contact saved", "success"));
			
			
		} catch (Exception e ) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went worng! Try again", "danger"));
		}
		
		return "user/contact_form";
	}
	
	@GetMapping("/view-contacts/{page}")
	public String viewContactPage(@PathVariable("page") Integer page, Model model, Principal principal) {
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUsername(username);
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = this.contactRepository.getContactByUserId(user.getId(), pageable);
		
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		return "user/view_contacts";
	}
	
	@GetMapping("/contact/{cid}")
	public String viewContact(@PathVariable("cid") Integer cid, Model model) {
		
		User user = (User) model.getAttribute("user");
		
		Optional<Contact> findById = this.contactRepository.findById(cid);
		Contact contact = findById.get();
		
		if(user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
		}
		
		return "user/contact";
	}
	
	@GetMapping("/contact/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, HttpSession session) {
		
		User user = (User) model.getAttribute("user");
		Contact contact = this.contactRepository.findById(cid).get();
		
		if(user.getId() == contact.getUser().getId()) {
			this.contactRepository.delete(contact);
			session.setAttribute("message", new Message("Contact deleted!", "success"));
			
		}else {
			session.setAttribute("message", new Message("Unauthorized URL!", "danger"));
		}
		
		return "redirect:/user/view-contacts/0";
	}
	
}
