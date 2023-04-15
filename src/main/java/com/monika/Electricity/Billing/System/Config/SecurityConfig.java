package com.monika.Electricity.Billing.System.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl(); 
	}

    @Bean
    BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    @Bean
	DaoAuthenticationProvider getDaoAuthProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(); 
		daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return daoAuthenticationProvider;
	}

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
    	http.csrf().disable()
    		.authorizeHttpRequests()
    		.requestMatchers("/createUser","/resources/**","/assets/**").permitAll()
    		.anyRequest().authenticated()
    		.and()
    		.formLogin()
    		.loginPage("/login")     //custom login page
    		.permitAll();            //permit custom login page
    		
    		/*
    		.requestMatchers("/admin/**")
    		//.hasRole("ADMIN")
    		.authenticated()
    		.and()
    		.authorizeHttpRequests()
    		.requestMatchers("/customer/**")
    		//.hasRole("CUSTOMER")
    		.authenticated()
    		.and()
    		.formLogin()
    		.and()
    		.authorizeHttpRequests()
    		.requestMatchers("/")
    		.permitAll()
    		*/	
 
    	/*
    	http
        	
                .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/customer").hasAnyRole("CUSTOMER", "ADMIN")				
                .requestMatchers("/","/createUser","/resources/**","/assets/**").permitAll()
                .anyRequest().authenticated()
                ).formLogin();
                
//                .formLogin(form -> form
//            			.loginPage("/")
//            			.permitAll()
//            		)
//                .csrf().disable();
 * */
 
        http
                .authenticationProvider(getDaoAuthProvider());
        return http.build();
    }

}
