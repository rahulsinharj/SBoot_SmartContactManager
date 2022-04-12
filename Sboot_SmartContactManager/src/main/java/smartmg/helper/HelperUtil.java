package smartmg.helper;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class HelperUtil {

	public int getRandomOTP() 
	{
		// Generating OTP of 6 digits :
		Random random = new Random(); // Using random method

		String otpStr = "";
		for (int i = 0; i < 6; i++) {
			otpStr += random.nextInt(10);
		}
		int otp = Integer.parseInt(otpStr); 
		System.out.println("OTP : "+otp);
		
		return otp;	
	}
}
