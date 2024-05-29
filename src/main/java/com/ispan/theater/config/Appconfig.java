package com.ispan.theater.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.ispan.theater.filter.JwtAuthenticationFilter;


@Configuration
@EnableCaching
@EnableWebSecurity
public class Appconfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Bean
    JwtAuthenticationFilter JwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable())
        .addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests((authorize) -> {
        	authorize.requestMatchers("/user/**","/order-redirect","/movie/linePayConfirm","/movie/findMovie","/movie/findAllCinema","/movie/dates","/movie/times","/movie/ecPayConfirm","/movie/tickets").permitAll().anyRequest().authenticated();
//          authorize.anyRequest().permitAll();
        });
        return http.build();
    }
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
