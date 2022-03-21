package smartmg.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import smartmg.dao.UserRepository;
import smartmg.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		String loginUserName = principal.getName();				// Get the user(user email) through Principal obj
		System.out.println("USER Email : "+ loginUserName);
		
//		User user = userRepository.getUserByUserEmail(loginUserName);
		User user = userRepository.findByEmail(loginUserName);
		System.out.println("USER info : "+user);
		
		model.addAttribute(user);
		
		return "normal/user_dashboard";				// that means template folder ke inside >> "normal" folder ke andar "user_dashboard.html" milega 
	}
	
		
	
}
