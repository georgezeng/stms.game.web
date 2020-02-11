package com.geozen.game.stms.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.geozen.game.stms.web.filter.ErrorHandlerFilter;
import com.geozen.game.stms.web.filter.LoggingFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private ErrorHandlerFilter errorHandlerFilter;
	@Autowired
	private LoggingFilter loggingFilter;
	
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	protected SuperAdminProperties adminProperties;
	
//	@Value("${access.control.allow.origin}")
//	private String origin;
//
//	private CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList(origin));
//		configuration.setAllowedHeaders(getAllowHeaders());
//		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
//		configuration.setAllowCredentials(true);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
//
//	protected List<String> getAllowHeaders() {
//		return Arrays.asList("*");
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().configurationSource(corsConfigurationSource());
		http.csrf().disable();
		http.httpBasic().disable();
		http.formLogin().permitAll();
		http.userDetailsService(userDetailsService);
		http.authorizeRequests().antMatchers("/actuator/health").permitAll();
		http.authorizeRequests().antMatchers("/actuator/**").authenticated();
		http.authorizeRequests().anyRequest().permitAll();
		http.addFilterBefore(errorHandlerFilter, ChannelProcessingFilter.class);
		http.addFilterAfter(loggingFilter, ChannelProcessingFilter.class);
	}
	
	@PostConstruct
	public void init() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername(adminProperties.getUsername())
				.password(pwdEncoder.encode(adminProperties.getPassword())).roles("AUTH_USER").build());
		userDetailsService = manager;
	}

}
