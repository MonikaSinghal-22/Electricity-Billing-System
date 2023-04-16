package com.monika.Electricity.Billing.System.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	
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
    		.authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/customer/**").hasAnyRole("CUSTOMER", "ADMIN")				
                    .requestMatchers("/createUser","/resetPassword","/resources/**","/assets/**").permitAll()
                    .anyRequest().authenticated()
                    )
    				.formLogin(form -> form
    						.loginPage("/login")
    						.permitAll()
    						//.defaultSuccessUrl("/customer/",true)
    						.successHandler(successHandler)
    				);
 
        http.authenticationProvider(getDaoAuthProvider());
        return http.build();
    }

}
