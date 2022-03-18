package smartmg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity				// isse SpringSecurity ki sari configurations enable hojaye.
public class SecurityConfig extends WebSecurityConfigurerAdapter  {				// Yaha pe hum ye batayege ki kaun se URL pattern ko protect karna hai ::

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider getDaoAuthenticationProvider() 
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(this.getPasswordEncoder());
		
		return daoAuthenticationProvider;
	}

	
	/* Configuring method ::
	-------------------------
	 	Builder ko hame batana hota hai ki hamlog kis tarah ka authentication use kar rahe hai ::																														
		whether we are using :	1. DataBase authentication , or
		 					 	2. In-memory Authentication  
		 =>	waisa hi method yaha pe call karna hoga.
	*/
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.authenticationProvider(this.getDaoAuthenticationProvider());			
	}

	
	/* Configuring Route method ::
	-------------------------
	 	Iss method se hamlog spring security ko ye batayege ki aap sare routs protect mat karo, jo humlog specifically bata rahe hai, bas usko protect karo.
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
								.antMatchers("/user/**").hasRole("USER")
								.antMatchers("/**").permitAll().and().formLogin().loginPage("/login")
								.and().csrf().disable();
	}
	
}
