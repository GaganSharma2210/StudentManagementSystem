package com.manya.studentManagementSystem.security;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.manya.studentManagementSystem.service.StudentService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private Environment environment;
	private StudentService studentService;

	private SecretKey secretKey;

	private String subject;

	public JwtTokenFilter(Environment environment, StudentService studentService) {
		super();
		this.environment = environment;
		this.studentService = studentService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		String jwt = null;

		if (authorization != null && authorization.startsWith("Bearer ")) {
			jwt = authorization.substring(7);

			if (isValid(jwt) && subject != null) {
				UserDetails user = studentService.loadUserByUsername(subject);

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
						null, user.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			}
		}
		filterChain.doFilter(request, response);

	}

	private boolean isValid(String jwt) {
		boolean isValid = true;

		String tokenSecret = environment.getProperty("token.secret");
		byte[] secretKeysBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
		secretKey = new SecretKeySpec(secretKeysBytes, SignatureAlgorithm.HS512.getJcaName());

		JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();

		try {
			Jwt<Header, Claims> parsedToken = (Jwt<Header, Claims>) jwtParser.parse(jwt);
			subject = parsedToken.getBody().getSubject();
		} catch (Exception ex) {
			isValid = false;
			ex.printStackTrace();
		}
		if (subject == null || subject.isEmpty()) {
			isValid = false;
		}

		return isValid;

	}

}
