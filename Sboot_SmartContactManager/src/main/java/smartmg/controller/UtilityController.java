package smartmg.controller;

import java.security.Principal;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

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
import smartmg.service.EmailService;
import smartmg.util.HelperUtil;
import smartmg.util.ResponseMessage;

@Controller
public class UtilityController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private HelperUtil helperUtil;
	
	@Autowired
	private EmailService emailService;
	
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

//====================# Showing Forgot Password :============================================	
	
	@RequestMapping("/forgot")
	public String openForgotForm()
	{
		return "forgot_email_form";
	}

//====================# Forgot Password Handler :============================================	
			
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session)
	{
		System.out.println("Forgot Email : "+email);
		
		final int OTP = this.helperUtil.getRandomOTP();
		
	// Program for sending OTP to email ::	
		
		User userCheck = this.userRepository.findByEmail(email);
		System.out.println("userCheck : "+userCheck);
		
		if(userCheck != null) 					// Matlab ki ye ek registered user hai, since verifying from DB
		{
			session.setAttribute("myotp", OTP);		// Storing OTP & EMAIL in session
			session.setAttribute("myemail", email);	
			
			boolean emailSentStatus = this.emailService.sendEmail(email, userCheck.getName(), OTP);
			if(emailSentStatus) {				// Matlab ki OTP bhej chuka hai successfully
				return "verify_otp";
			}	
			else {
				session.setAttribute("message", new ResponseMessage("Email unable to sent! Please try again later ! " , "alert-danger"));
				return "redirect:/forgot" ;
			}
		}
		else {
			session.setAttribute("message", new ResponseMessage("Sorry! You are not a registered user ! " , "alert-danger"));
			return "redirect:/forgot" ;
		}
		
	}

//====================# Verify OTP Handler :============================================	
			
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp)
	{
		System.out.println("OTP entered by user : "+otp);
		return "";
	}
		
}
