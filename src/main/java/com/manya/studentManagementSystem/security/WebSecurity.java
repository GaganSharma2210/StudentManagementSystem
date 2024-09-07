package com.manya.studentManagementSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.manya.studentManagementSystem.service.StudentService;

@Configuration
@EnableWebSecurity
public class WebSecurity {

	private Environment environment;
	private StudentService studentService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(Environment environment, StudentService studentService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.environment = environment;
		this.studentService = studentService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		// Configure AuthenticationManagerBuilder
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(studentService).passwordEncoder(bCryptPasswordEncoder);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		// Create AuthenticationFilter
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(studentService, environment,
				authenticationManager);
		JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(environment,studentService);
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));

		http.csrf((csrf) -> csrf.disable());

		http.authorizeHttpRequests((authz) -> authz.requestMatchers(new AntPathRequestMatcher("/students", "POST"))
				.permitAll().requestMatchers(new AntPathRequestMatcher("/students/status/check")).permitAll()
				.anyRequest().authenticated()).addFilter(authenticationFilter).addFilter(authenticationFilter)
				.authenticationManager(authenticationManager)
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.getOrBuild();

	}

}
