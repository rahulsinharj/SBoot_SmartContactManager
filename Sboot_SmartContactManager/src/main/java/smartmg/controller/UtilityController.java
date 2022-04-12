package smartmg.controller;

import java.security.Principal;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import smartmg.dao.ContactRepository;
import smartmg.dao.UserRepository;
import smartmg.entity.Contact;
import smartmg.entity.User;
import smartmg.helper.HelperUtil;

@Controller
public class UtilityController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private HelperUtil helperUtil;
	
//====================# Search Handler :=====================================================			
	
	@GetMapping("/search/{query}")
	@ResponseBody
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal)
	{
		User onuser = userRepository.findByEmail(principal.getName());
		
		//	System.out.println("Query : "+query);
		
		List<Contact> searchContacts = this.contactRepository.findByNameContainingAndUser(query, onuser);
		
		return ResponseEntity.ok(searchContacts);
		
	}

//====================# Forgot Password Handler :============================================	
	
	@RequestMapping("/forgot")
	public String openForgotForm()
	{
		return "forgot_email_form";
	}

//====================# Forgot Password Handler :============================================	
			
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email)
	{
		System.out.println("Forgot Email : "+email);
		
		final int OTP = this.helperUtil.getRandomOTP();
		
	// Program for sending OTP to email ::	
		
		return "verify_otp";
	}
	
		
}
