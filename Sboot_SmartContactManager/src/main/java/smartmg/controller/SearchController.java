package smartmg.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import smartmg.dao.ContactRepository;
import smartmg.dao.UserRepository;
import smartmg.entity.Contact;
import smartmg.entity.User;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal)
	{
		User onuser = userRepository.findByEmail(principal.getName());
		
		//	System.out.println("Query : "+query);
		
		List<Contact> searchContacts = this.contactRepository.findByNameContainingAndUser(query, onuser);
		
		return ResponseEntity.ok(searchContacts);
		
	}
	
}
