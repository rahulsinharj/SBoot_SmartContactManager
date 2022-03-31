package smartmg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity				// isse SpringSecurity ki sari configurations enable hojaye.
public class SecurityConfig extends WebSecurityConfigurerAdapter  {				// Yaha pe hum ye batayege ki kaun se URL pattern ko protect karna hai ::

//	@Bean													// Iss bean ko comment isliye kar diye because "MyUserDetailsService" class ko already @Server (i.e, @Component) bana chuke hai, jisse ki uska obj ab auto create hojayega by Spring, Only we need to fetch it by Autowired. 
//	public UserDetailsService getUserDetailsService() {
//		return new MyUserDetailsService();
//	}
	
	// Or in place of above , we can also make UserDetailsServiceImpl as @Service annotated , so that uska obj khud hi spring bana de, and iss class uske reference ko @AutoWired ke karan uska obj mil jayega. 
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() 
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		
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
		auth.authenticationProvider(this.daoAuthenticationProvider());			
	}

	
/* Configuring Route method ::
   -------------------------
	 	Iss method se hamlog spring security ko ye batayege ki aap sare URL-routes protect mat karo, jo humlog specifically bata rahe hai, bas usko protect karo.
*/
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
								.antMatchers("/user/**").hasRole("USER")
								.antMatchers("/**").permitAll()
								.and().formLogin()							// http basic based authentication nhi hoga, balki proper form based hoga 
								.loginPage("/login")						// loginPage("/login") - is for "our own Custom LOGIN/Signin" page based Login authentication
								.loginProcessingUrl("/dologin")				// loginProcessingUrl() - is the URL to submit the username and password, isse hum wo URL bayatege jis URL pe hum username&password credentials ko bhej rahe hai.		
								.defaultSuccessUrl("/user/index")			// defaultSuccessUrl() - is the landing page after a successful login.
//								.failureUrl("/loginfail")					// failureUrl() - is the landing page after an unsuccessful login
								.and().csrf().disable();					// For disabling this we should mention this line inside login page : <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
	}
	
} 
