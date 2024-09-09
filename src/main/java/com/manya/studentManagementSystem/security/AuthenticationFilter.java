package com.manya.studentManagementSystem.security;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manya.studentManagementSystem.model.LoginRequestModel;
import com.manya.studentManagementSystem.service.StudentService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private Environment environment;
	private StudentService studentService;
	
	public AuthenticationFilter(StudentService studentService, Environment environment, AuthenticationManager authManager) {
		super(authManager);
		this.studentService = studentService;
		this.environment = environment;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
		
			LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(creds.getContactNumber(), creds.getPassword(), new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) {

		String userName = ((User) auth.getPrincipal()).getUsername();

		String tokenSecret = environment.getProperty("token.secret");

		byte[] secretKeysBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
		SecretKey secretKey = new SecretKeySpec(secretKeysBytes, SignatureAlgorithm.HS512.getJcaName());
		Instant now = Instant.now();
		String token = Jwts.builder().subject(userName)
				.expiration(Date.from(
						now.plusSeconds(Long.parseLong(environment.getProperty("token.expiration.time.seconds")))))
				.issuedAt(Date.from(now)).signWith(secretKey, SignatureAlgorithm.HS512).compact();

		res.addHeader("token", token);
		JSONObject jobj = new JSONObject();
		jobj.put("jwt", token);
		try {
			res.getWriter().write(jobj.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed)  {
		ObjectMapper objectMapper = new ObjectMapper();
		
		response.addHeader("timestamp", LocalDateTime.now().toString());
		response.addHeader("exception", failed.getMessage());


		throw new RuntimeException(failed.getMessage(), failed.getCause());

	}


}
