package com.activedge.report.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.activedge.report.constant.CustomConstants;
import com.activedge.report.security.ProfileDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private ProfileDetailsService profileService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**").permitAll().
		antMatchers("/assets/**/css/**").permitAll().
		antMatchers("/assets/**/js/**").permitAll().
		antMatchers("/assets/**/img/**").permitAll().
		antMatchers("/javax.faces.resource/*").permitAll().		
		antMatchers("/assets/**/plugins/**").permitAll().
		antMatchers("/assets/**/fonts/**").permitAll().
		antMatchers("/pages/login.xhtml").permitAll().
		antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')").
		anyRequest().authenticated().
		and().formLogin().  //login configuration
        loginPage("/pages/login.xhtml").
        loginProcessingUrl("/login").
        usernameParameter("username").
        passwordParameter("password").
        defaultSuccessUrl("/pages/home.xhtml").	
		and().logout().    //logout configuration
		logoutUrl("/logout"). 
		logoutSuccessUrl("/pages/login.xhtml");
		
		http.csrf().disable();

	} 
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(profileService).
			passwordEncoder(new StandardPasswordEncoder(CustomConstants.PASSWORD_KEY_ENCODER));
	}
} 
