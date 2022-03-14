package smartmg.controller;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import smartmg.entity.User;

@Controller
public class HomeController {

	@RequestMapping({"/" , "/home"}) 
	public String home() 
	{
		return "home";
	}
	
	@RequestMapping("/about") 
	public String about() 
	{
		return "about";
	}
	
	@RequestMapping("/signup") 
	public String signup(Model model) 
	{
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/do-register")
	public String registerUser(@ModelAttribute("user") User user, 
								@RequestParam(value = "agreementCheck", defaultValue = "false") boolean agreementCheck,
								Model model) 
	{
		System.out.println("Agreement : "+agreementCheck);
		System.out.println("USER : "+user);

		return "signup";
	}
	
}
