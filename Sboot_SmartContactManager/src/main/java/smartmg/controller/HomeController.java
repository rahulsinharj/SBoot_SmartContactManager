package smartmg.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import smartmg.dao.UserRepository;
import smartmg.entity.User;
import smartmg.util.ResponseMessage;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping({"/" , "/home"}) 
	public String home() 
	{
		return "home";
	}
	
	@RequestMapping(path = "/about") 
	public String about() 
	{
		return "about";
	}
	
//	@RequestMapping("/error")
//	public String error(HttpServletRequest request, Model model)
//	{
//		Exception exception = (Exception)request.getAttribute("javax.servlet.error.exception");
//		model.addAttribute("exception", exception);
//		return "error";
//	}
	
	@RequestMapping("/login")
	public String customLogin(Principal principal)
	{
		if(principal!=null) {				// Checking whether any loggedin-User is in memory of Principal obj.
			return "redirect:/user/index";
		}
		else {
			return "login";
		}
	}
	
	@RequestMapping("/signup") 
	public String signup(Model model) 
	{
		model.addAttribute("user", new User());
		return "signup";
	} 

//=======================# Registering any new user :================================	
	@PostMapping("/do-register")
	public String registerUser(@Valid @ModelAttribute("user") User user,  BindingResult result,
								@RequestParam(value = "agreementCheck", defaultValue = "false") boolean agreementCheck,
								Model model , HttpSession session) 
	{
		try 
		{
			if(result.hasErrors()) {
				System.out.println("ERROR : "+result.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			if(!agreementCheck) {
				System.out.println("You have not agreed terms and conditions ! ");
				throw new Exception("You have not agreed terms and conditions ! ");
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("contact.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement : "+agreementCheck);
			System.out.println("USER : "+user);
			
			User savedUser = this.userRepository.save(user);
			
			model.addAttribute("user" , new User());
			session.setAttribute("message", new ResponseMessage("Successfully Registered !! ", "alert-success"));
			
			return "signup";
		} 
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user" , user);
			session.setAttribute("message", new ResponseMessage("something went wrong : "+e.getMessage() , "alert-danger"));
			return "signup";
		}
		
	}
	
}
