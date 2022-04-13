package smartmg.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import smartmg.dao.UserRepository;
import smartmg.entity.User;
import smartmg.service.EmailService;
import smartmg.util.HelperUtil;
import smartmg.util.ResponseMessage;

@Controller
public class EmailForgotController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HelperUtil helperUtil;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

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
			session.setAttribute("genOtp", OTP);		// Storing Geenrated-OTP & Entered-EMAIL in session
			session.setAttribute("myemail", email);	
			
			boolean emailSentStatus = this.emailService.sendEmail(email, userCheck.getName(), OTP);
			if(emailSentStatus) 				// Matlab ki OTP bhej chuka hai successfully
			{
				session.setAttribute("message", new ResponseMessage("We have sent OTP to your Email ! ", "alert-success"));
				return "verify_otp_form";
			}	
			else 
			{
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
	public String verifyOtp(@RequestParam("otp") int enteredOtp, HttpSession session)
	{
		System.out.println("OTP entered by user : "+enteredOtp);
		
		int genOtp = (int)session.getAttribute("genOtp");
		
		if(enteredOtp == genOtp)
		{
		// Showing Password-Change-Form , Once OTP is verified ::
			System.out.println("OTP Verified !");
			return "forgot_password_change_form";			// in dono hi cases me url = /verify-otp  hi rahega, kyuki iss url ke liye below mentioned pages return horahi hai 
		}
		else {
			session.setAttribute("message", new ResponseMessage("Wrong OTP entered ! Try again ! " , "alert-danger"));
			return "verify_otp_form";
		}
		
	}
		
//====================# Changing passsord after email and OTP verification :============================================	
	
	@PostMapping("/change-password")	
	public String changePassword(@RequestParam("newPassword") String newPassword, HttpSession session)
	{
		String myemail = (String)session.getAttribute("myemail");
		System.out.println("myemail : "+myemail);
		
		return "";
	}
	
}
