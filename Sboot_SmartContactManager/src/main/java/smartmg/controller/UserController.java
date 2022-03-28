package smartmg.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import smartmg.dao.UserRepository;
import smartmg.entity.Contact;
import smartmg.entity.User;
import smartmg.helper.ResponseMessage;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	
	private int imgcount;
	
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
	public String processContact(@Valid @ModelAttribute("contact") Contact contact,  BindingResult result,
								 @RequestParam("profileImage") MultipartFile imgfile,  
								 Principal principal , HttpSession session)
	{
		try {
			System.out.println("DATA : "+contact);
			
			String onuserEmail = principal.getName();
			User onuser = this.userRepository.findByEmail(onuserEmail);
			contact.setUser(onuser);
			
			// processing and uploading file
			if(imgfile.isEmpty()) {
				System.out.println("Image file is empty !");
			}
			else 
			{
//				contact.setImage(imgfile.getOriginalFilename());
				
				// Appending "imgcount" after the filename before the .jpeg extension.
				imgcount++;
				String tempName = imgfile.getOriginalFilename();
				String imageFileType = tempName.substring(tempName.indexOf("."));
				String imageFileName = tempName.substring(0,tempName.indexOf(".")) + imgcount + imageFileType;
						System.out.println("imageFileName : "+imageFileName);
				
				contact.setImage(imageFileName);
				
				
/*				File uploadFilePath = new ClassPathResource("static/img").getFile();	//	"\target\classes\static\img\"
						System.out.println("uploadFilePath : " +uploadFilePath);
				
				Path imgSavePath = Paths.get(uploadFilePath.getAbsolutePath()+File.separator+imgfile.getOriginalFilename());
						System.out.println("imgSavePath : " +imgSavePath);		//	 E:\Stu\Code Files\GIT Eclipse Files\SBoot_SmartContactManager\Sboot_SmartContactManager\target\classes\static\img\facebook.png
*/				
				
				final String UPLOAD_IMG_DIR = Paths.get("src/main/resources/static/img").toAbsolutePath().toString();	// 	"\src\main\resources\static\img\"
				
//				Path imgSavePath = Paths.get(UPLOAD_IMG_DIR + File.separator + imgfile.getOriginalFilename());
				Path imgSavePath = Paths.get(UPLOAD_IMG_DIR + File.separator + imageFileName);
						System.out.println("imgSavePath : " + imgSavePath);			//	 E:\Stu\Code Files\GIT Eclipse Files\SBoot_SmartContactManager\Sboot_SmartContactManager\src\main\resources\static\img\baloon.jpeg
						
				Files.copy(imgfile.getInputStream(), imgSavePath, StandardCopyOption.REPLACE_EXISTING);		// {input , target , options-how to write whether replace }
				
			
				onuser.getContacts().add(contact);
				this.userRepository.save(onuser);
				
				System.out.println(contact.getName() +" has been added to DataBase");
				// Message Success
				session.setAttribute("message", new ResponseMessage("Contact Name : " +contact.getName() +" Successfully Added !! ", "alert-success"));
				
			}
		} 
		catch (Exception e) {
			// Message Error
			session.setAttribute("message", new ResponseMessage("something went wrong : "+e.getMessage() , "alert-danger"));
			e.printStackTrace();
		}
		
		return "normal/add_contact_form";
	}
	
	
	
}
