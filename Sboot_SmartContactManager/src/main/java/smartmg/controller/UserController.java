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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import smartmg.dao.ContactRepository;
import smartmg.dao.UserRepository;
import smartmg.entity.Contact;
import smartmg.entity.User;
import smartmg.helper.ResponseMessage;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	private int imgcount;
	
	private User onuser;		// Logged in User

//====================# 1ST-Method for adding common data to response :==========================	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		String loginUserEmail = principal.getName();				// Get the user(user email) through Principal obj
		System.out.println("USER Email : "+ loginUserEmail);
		
	//	this.onuser = userRepository.getUserByUserEmail(loginUserName);
		this.onuser = userRepository.findByEmail(loginUserEmail);
		
		System.out.println("USER Info : " + onuser);
		model.addAttribute("user", onuser);			// By this line, "user" obj will be accessible in all user/index html pages 
	}
	
	

//====================# Dashboard home :=====================================================		
	@RequestMapping("/index")
	public String dashboard()
	{
		return "normal/user_dashboard";				// that means template folder ke inside >> "normal" folder ke andar "user_dashboard.html" milega 
	}
	
	
//====================# Add Form Handler :==================================================		
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) 
	{
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
//====================# Processing AddContact Form :========================================	
	
	@PostMapping("process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact,  BindingResult bindingResult,
								 @RequestParam("profileImage") MultipartFile imgfile, HttpSession session)
	{
		try {
			System.out.println("DATA : "+contact);
			
			contact.setUser(this.onuser);
			
	// processing and uploading file
			if(imgfile.isEmpty()) {
				System.out.println("Image file is empty !");
				contact.setImage("contact.jpg");
			}
			else 
			{
				// contact.setImage(imgfile.getOriginalFilename());
				
	// Appending "imgcount" after the filename before the .jpeg extension.
				imgcount++;
				String tempName = imgfile.getOriginalFilename();
				String imageFileType = tempName.substring(tempName.indexOf("."));
				String imageFileName = tempName.substring(0,tempName.indexOf(".")) + imgcount + imageFileType;
						System.out.println("imageFileName : "+imageFileName);
				
				contact.setImage(imageFileName);

	// Saving img into location :: 	"\target\classes\static\img\"		
				
/*				File uploadFilePath = new ClassPathResource("static/img").getFile();	//	"\target\classes\static\img\"
					System.out.println("uploadFilePath : " +uploadFilePath);
				
			//	Path imgSaveFilePath = Paths.get(uploadFilePath + File.separator + imgfile.getOriginalFilename());
				Path imgSaveFilePath = Paths.get(uploadFilePath.getAbsolutePath()+File.separator + imageFileName);
				
					System.out.println("imgSaveFilePath : " +imgSaveFilePath);		//	 E:\Stu\Code Files\GIT Eclipse Files\SBoot_SmartContactManager\Sboot_SmartContactManager\target\classes\static\img\facebook.png
*/
	
	// Saving img into location :: 	"\src\main\resources\static\img\"
				
				final String uploadFilePath = Paths.get("src/main/resources/static/img").toAbsolutePath().toString();	// 	"\src\main\resources\static\img\"
						
				Path imgSaveFilePath = Paths.get(uploadFilePath + File.separator + imageFileName);
					System.out.println("imgSaveFilePath : " + imgSaveFilePath);			//	 E:\Stu\Code Files\GIT Eclipse Files\SBoot_SmartContactManager\Sboot_SmartContactManager\src\main\resources\static\img\baloon.jpeg
				
				Files.copy(imgfile.getInputStream(), imgSaveFilePath, StandardCopyOption.REPLACE_EXISTING);		// {input , target , options-how to write whether replace }
				
			}
			
			// Anyways we will save the contact to DB, despite if he has uploaded or not.
			onuser.getContacts().add(contact);
			this.userRepository.save(onuser);
				
			System.out.println(contact.getName() +" has been added to DataBase");
			// Message Success
			session.setAttribute("message", new ResponseMessage("Contact Name : " +contact.getName() +" is Successfully Added !! ", "alert-success"));
				
			
		} 
		catch (Exception e) {
			// Message Error
			session.setAttribute("message", new ResponseMessage("something went wrong : "+e.getMessage() , "alert-danger"));
			e.printStackTrace();
		}
		
		return "redirect:/user/add-contact";
	}
	
//====================# Show Contact Form :=================================================
	/*
		Per Page = 5[n]
		Current Page = 0[page]
	*/
	
	@GetMapping("/view-contacts/{page}") 
	public String viewContacts(@PathVariable("page") int page, Model model)
	{
	// Contacts ki list ko bhejni hai ::
		
		// List<Contact> contacts = onuser.getContacts();		// This is also one way to get all contacts associated in that onuser. This apporach won't work with Pagination.
		// List<Contact> contacts = this.contactRepository.findContactsByUser(onuser.getId());		// Getting all contacts without Pagination.
		
		Pageable pageable = PageRequest.of(page, 4);		// Pageable asks for two information : 1) Current Page- page, 2) Contact Per Page- eg-4
		Page<Contact> pageContacts = this.contactRepository.findContactsByUser(this.onuser.getId(), pageable);
		
		model.addAttribute("allContacts", pageContacts);	// Sending this model to "view_contact.html" page.
		model.addAttribute("currentPage", page);	
		model.addAttribute("totalPages", pageContacts.getTotalPages());
		
		return "normal/view_contacts";
	}
	
//====================# Showing specific Contact Detail :===================================
	
	@GetMapping("/contact/{cId}")
	public String showContactDetails(@PathVariable("cId") int cId, Model model)
	{
		System.out.println("Contact Id : "+cId);
		
		Contact contact = this.contactRepository.findById(cId).get();
		System.out.println(contact);
		
	//======# Restricting onuser for seeing other users contact :=======	
		
		if(this.onuser.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact",contact);	
		}
		
			
		return "normal/contact_details";
	}

//====================# Deleting a contact :================================================
	
	@GetMapping("/delete-contact/{cid}")
	@Transactional
	public String deleteContact(@PathVariable("cid") int cid, HttpSession session)
	{
		try {
			
			Contact contact = this.contactRepository.findById(cid).get();
			String cname = contact.getName();
				System.out.println("Delete Contact : "+contact);
			
			if(this.onuser.getId()==contact.getUser().getId())
			{
			// Removing that contact's Image from /img folder
				//File uploadFilePath = new ClassPathResource("static/img").getFile();									//	"\target\classes\static\img\"
				//Path imgDelFilePath = Paths.get(uploadFilePath.getAbsolutePath()+File.separator + contact.getImage());
				
				final String uploadFilePath = Paths.get("src/main/resources/static/img").toAbsolutePath().toString();	// 	"\src\main\resources\static\img\"
				Path imgDelFilePath = Paths.get(uploadFilePath + File.separator + contact.getImage());
						System.out.println("File deleted at imgDelPath : " +imgDelFilePath);		//	 E:\Stu\Code Files\GIT Eclipse Files\SBoot_SmartContactManager\Sboot_SmartContactManager\target\classes\static\img\facebook.png
		
				
				System.out.println("File deleted at imgDelPath : " +imgDelFilePath);		//	 E:\Stu\Code Files\GIT Eclipse Files\SBoot_SmartContactManager\Sboot_SmartContactManager\target\classes\static\img\facebook.png
		
				Files.deleteIfExists(imgDelFilePath);
				
			// Removing that contact
				// contact.setUser(null);							// Unlinking this contact with the user firstly.
				// this.contactRepository.delete(contact);			// If this approach doesn't work then follow below ::
	
				this.onuser.getContacts().remove(contact);		// User ke jitne bhi contact honge usse ye particular contact ko remove kar do.
				this.userRepository.save(this.onuser);			// iss onuser ke paas contacts bhi to hai, usko bhi update kar dega.
				
				
				session.setAttribute("message", new ResponseMessage("Contact Name : " +cname +" has been deleted Successfully !! ", "alert-success"));
			}
			else {
				session.setAttribute("message", new ResponseMessage("Unauthorized User Operation detected ! " , "alert-danger"));
			}
		} 
		catch (Exception e) {
			// Message Error
			session.setAttribute("message", new ResponseMessage("something went wrong : "+e.getMessage() , "alert-danger"));
			e.printStackTrace();
		}
		
		return "redirect:/user/view-contacts/0";
	}
	
//====================# Updating a contact :================================================
	
	@PostMapping("/update-contact/{cid}")
	public String updateContact(@PathVariable("cid") int cid, Model model)
	{
		System.out.println("Update form for Cid : "+cid);
		
		Contact contact = this.contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);
		
		return "normal/update_contact_form";
	}
			
	
//====================# Processing UpdateContact Form :=====================================	
	
	@PostMapping("process-update")
	public String processUpdateContact(@Valid @ModelAttribute("updatedContact") Contact updatedContact,  BindingResult bindingResult,
									 @RequestParam("profileImage") MultipartFile imgfile,  
									 HttpSession session, Model model)
	{
		try {
			
			System.out.println("Updated DATA : "+updatedContact);
				
			// Fetching Old Contact details for saving image if user hasn't send the updated image ::
			Contact oldContact = this.contactRepository.findById(updatedContact.getcId()).get();
			
			updatedContact.setUser(this.onuser);
				
			// processing and uploading image ::
			
			if(!imgfile.isEmpty()) 
			{
			// Delete old profile pic  ::
				
				// File uploadFilePath = new ClassPathResource("static/img").getFile();									//	"\target\classes\static\img\"
				// Path imgDelFilePath = Paths.get(uploadFilePath.getAbsolutePath()+File.separator + oldContact.getImage());
				
				final String uploadFilePath = Paths.get("src/main/resources/static/img").toAbsolutePath().toString();	// 	"\src\main\resources\static\img\"
				Path imgDelFilePath = Paths.get(uploadFilePath + File.separator + oldContact.getImage());
				
				Files.deleteIfExists(imgDelFilePath);
				
			// Store the new pic ::
				
				// updatedContact.setImage(imgfile.getOriginalFilename());		// Setting updated image without any count in end name.
				
			// Appending "imgcount" after the filename before the .jpeg extension ::
				imgcount++;
				String tempName = imgfile.getOriginalFilename();
				String imageFileType = tempName.substring(tempName.indexOf("."));
				String imageFileName = tempName.substring(0,tempName.indexOf(".")) + imgcount + imageFileType;
						System.out.println("imageFileName : "+imageFileName);
				
				updatedContact.setImage(imageFileName);
				
			// Saving the actual image into server ::
				Path imgSaveFilePath = Paths.get(uploadFilePath +File.separator + imageFileName);
						System.out.println("imgSaveFilePath : " +imgSaveFilePath);		//	 E:\Stu\Code Files\GIT Eclipse Files\SBoot_SmartContactManager\Sboot_SmartContactManager\target\classes\static\img\facebook.png
			
				Files.copy(imgfile.getInputStream(), imgSaveFilePath, StandardCopyOption.REPLACE_EXISTING);		// {input , target , options-how to write whether replace }
				
			}
			else {
				// If the contact has not updated any new profile pic
				updatedContact.setImage(oldContact.getImage());
			}
	
			// Anyways we will save the updated-contact to DB, despite if user has/or hasn't send the updated image.
			this.contactRepository.save(updatedContact);
					
			System.out.println(updatedContact.getName() +" has been updated to DataBase");
			// Message Success
			session.setAttribute("message", new ResponseMessage("Contact Name : " +updatedContact.getName() +" is Successfully Updated !! ", "alert-success"));
					
				
		} 
		catch (Exception e) {
			// Message Error
			session.setAttribute("message", new ResponseMessage("something went wrong : "+e.getMessage() , "alert-danger"));
			e.printStackTrace();
		}
			
		return "redirect:/user/contact/"+updatedContact.getcId();
	}	
	
	
//====================# Showing Onuser Profile Detail :========================================	
	
	@GetMapping("/profile")
	public String onuserProfile()
	{
		// Since user has been returned in each and every field , so now we only have to return "normal/profile .html" page.
		return "normal/profile";
	}
	
//====================# Changing the password - from Settings handler page :===========================	
	
	@GetMapping("/settings")
	public String openSettings()
	{
		return "normal/settings";
	}
	
	
}




