package smartmg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import smartmg.dao.UserRepository;
import smartmg.entity.User;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// Fetching User from Database ::
		User user = this.userRepository.getUserByUserName(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("Could not found User !!");
		}
		CustomUserDetails userDetails =  new CustomUserDetails(user);
 		
		return userDetails;
	}

}