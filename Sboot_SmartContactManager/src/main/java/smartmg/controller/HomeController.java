package smartmg.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import smartmg.dao.UserRepository;
import smartmg.entity.User;
import smartmg.helper.ResponseMessage;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
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
			user.setImageUrl("default.png");
			
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
