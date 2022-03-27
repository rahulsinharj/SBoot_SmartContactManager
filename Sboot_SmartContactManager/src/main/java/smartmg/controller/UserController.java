package smartmg.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import smartmg.dao.UserRepository;
import smartmg.entity.Contact;
import smartmg.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	// Method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		String loginUserName = principal.getName();				// Get the user(user email) through Principal obj
		System.out.println("USER Email : "+ loginUserName);
		
//		User user = userRepository.getUserByUserEmail(loginUserName);
		User user = userRepository.findByEmail(loginUserName);
		System.out.println("USER info : "+user);
		
		model.addAttribute(user);
	}
	
	// Dashboard home
	@RequestMapping("/index")
	public String dashboard()
	{
		return "normal/user_dashboard";				// that means template folder ke inside >> "normal" folder ke andar "user_dashboard.html" milega 
	}
	
	// Open Add Form Handler ::
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) 
	{
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	// Processing AddContact Form ::
	@PostMapping("process-contact")
	public String processContact(@ModelAttribute("contact") Contact contact, Principal principal)
	{
		System.out.println("DATA : "+contact);
		
		String onuserEmail = principal.getName();
		User onuser = this.userRepository.findByEmail(onuserEmail);
		
		contact.setUser(onuser);
		
		onuser.getContacts().add(contact);
		this.userRepository.save(onuser);
		
		
		return "normal/add_contact_form";
	}
	
	
	
}
