package smartmg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import smartmg.dao.UserRepository;
import smartmg.entity.User;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// Fetching User from Database ::
//		User user = this.userRepository.getUserByUserEmail(username);
		User user = this.userRepository.findByEmail(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("Could not found User !!");
		}
		MyUserDetails userDetails =  new MyUserDetails(user);
 		
		return userDetails;
	}

}
