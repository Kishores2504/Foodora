package com.FoodoraProject.FoodoraProject1.Security;


import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Component
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain config(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf->csrf.disable())
				.formLogin(form->form.disable())
				.cors(cors -> {})   
				.authorizeHttpRequests(auth->
				 auth.requestMatchers(HttpMethod.POST,"/user/register").permitAll()
				.requestMatchers(HttpMethod.POST,"/user/login").permitAll()
				.requestMatchers(HttpMethod.GET,"/user/profile").hasAnyRole("USER","ADMIN")
				.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/category/**").hasAnyRole("USER","ADMIN")
				.requestMatchers("/food/**").hasAnyRole("USER","ADMIN")
				.requestMatchers(HttpMethod.GET,"/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				)
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config=new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173"));
		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean 
	public AuthenticationManager authmanager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passencoder() {
		return new BCryptPasswordEncoder();
	}
}
