package smartmg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String signup() 
	{
		return "signup";
	}
	
	@PostMapping("/do-register")
	public String doRegister() 
	{
		return "register";
	}
	
}
