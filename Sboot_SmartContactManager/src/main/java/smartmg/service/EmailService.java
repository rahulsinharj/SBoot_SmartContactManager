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
	private String emailFrom;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public boolean sendEmail(String emailTo, String receiverName, int OTP) 
	{
		boolean emailSentStatus = false;
		try 
		{
			//	SimpleMailMessage mail = new SimpleMailMessage();
			//		mail.setFrom(fromEmail);
			//		mail.setTo(receiverMail);
			//		mail.setSubject("OTP Verification Mail");
			//		mail.setText(textMessage);
			//	mailSender.send(mail);
			
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			
			//String receiverName = emailTo.substring(0,emailTo.indexOf('@'));
			
			String textMessage = "<p>Hi "+receiverName +" , </p>"
								+ "<div style='border:1px solid #e2e2e2; padding:20px'>"
								+ "<h3>Please submit this OTP to your @Smart-Contact-Manager verification page !</h3>"
								+ "<h1>OTP is : "+OTP+"</h1>"
//								+ "<h3>Thank you from @Smart Contact Manager Team ! Have a nice day !</h3>"
								+ "</div>"; 
				
				//textMessage += "<img src='cid:contact_logo' style='height: 150px; border-radius:18%;'>";
				
			mimeMessageHelper.setFrom(emailFrom, "SmartContactManager Support Team");
			mimeMessageHelper.setTo(emailTo);
			mimeMessageHelper.setSubject("Spring OTP Verification Mail");
			mimeMessageHelper.setText(textMessage, true);
			
		// Sent Inline photo :
			//ClassPathResource resource = new ClassPathResource("/static/img/contact_logo.jpg");
			//mailHelper.addInline("contact_logo", resource);
			
			mailSender.send(mimeMessage);
			System.out.println("Mail sent to : "+emailTo);
			emailSentStatus = true;
			
		} 
		catch (MessagingException e) {
			e.printStackTrace();
			return emailSentStatus;
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return emailSentStatus;
		}
		
		return emailSentStatus ;
	}
}
