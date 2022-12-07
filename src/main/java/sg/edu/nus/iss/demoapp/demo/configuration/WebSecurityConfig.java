package sg.edu.nus.iss.demoapp.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Deprecated
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService uds;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(uds).passwordEncoder(encoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/home", "/addnew", "/save").permitAll()
				.antMatchers("/welcome").authenticated()
				// .antMatchers("/admin", "/memberlisting").hasRole("Admin")
				.antMatchers("/admin", "/memberlisting").hasAuthority("Admin")
				.antMatchers("/mgr").hasAuthority("Manager")
				// .antMatchers("/emp").hasRole("Employee")
				.antMatchers("/emp").hasAuthority("Employee")
				.antMatchers("/hr").hasAuthority("HR")
				.antMatchers("/common").hasAnyAuthority("Employee,Manager,Admin")
				.anyRequest().authenticated()

				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.defaultSuccessUrl("/welcome", true)

				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

				.and()
				.exceptionHandling()
				.accessDeniedPage("/accessDenied");

	}

	// @Configuration
	// protected static class AuthenticationConfiguration extends
	// GlobalAuthenticationConfigurerAdapter {

	// @Override
	// public void init(AuthenticationManagerBuilder auth) throws Exception {
	// auth.inMemoryAuthentication().withUser("ravan").password("ravan123").roles("EMPLOYEE");
	// auth.inMemoryAuthentication().withUser("ram").password("ram123").roles("ADMIN");

	// }
	// }

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		// GrantedAuthority authority = new SimpleGrantedAuthority("Admin");
		// UserDetails userDetails = (UserDetails) new User("ram", "ram123",
		// Arrays.asList(authority));
		// return new InMemoryUserDetailsManager(Arrays.asList(userDetails));

		UserDetails admin = User.withUsername("ram")
				.password(encoder.encode("ram123"))
				.roles("Admin")
				.build();
		UserDetails employee = User.withUsername("ravan")
				.password(encoder.encode("ravan123"))
				.roles("Employee")
				.build();
		return new InMemoryUserDetailsManager(admin, employee);
	}

}
