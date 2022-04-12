package smartmg.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String receiverMail, int OTP) 
	{
		try 
		{
			//	SimpleMailMessage mail = new SimpleMailMessage();
			//		mail.setFrom(fromEmail);
			//		mail.setTo(receiverMail);
			//		mail.setSubject("OTP Verification Mail");
			//		mail.setText(textMessage);
			//	mailSender.send(mail);
			
			MimeMessage mail = mailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true);
			
			String receiverName = receiverMail.substring(0,receiverMail.indexOf('@'));
			String textMessage = "<p>Hi "+receiverName +" , </p>";
				textMessage += "<h3>Please submit this OTP to your Smart-Contact-Manager verification page !</h3>";
				textMessage += "<h1>OTP is : "+OTP+"</h1>";
				//textMessage += "<hr><img src='cid:contact_logo'>";
				
			mailHelper.setFrom(fromEmail, "Rahul Info");
			mailHelper.setTo(receiverMail);
			mailHelper.setSubject("Spring OTP Verification Mail");
			mailHelper.setText(textMessage, true);
			
			// Sent Inline photo :
			ClassPathResource resource = new ClassPathResource("/static/img/contact_logo.jpg");
			mailHelper.addInline("contact_logo", resource);
			
			mailSender.send(mail);
			System.out.println("Mail sent to : "+receiverMail);
			
		} 
		catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
}
