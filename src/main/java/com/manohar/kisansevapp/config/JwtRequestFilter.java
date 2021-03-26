package com.manohar.kisansevapp.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manohar.kisansevapp.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private ObjectMapper mapper;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		Map<String, Object> errorDetails = new HashMap<>();
		try {

			errorDetails.put("message", "Invalid token");

			String authorizationHeader = httpServletRequest.getHeader("Authorization");

			String token = null;
			String userName = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7);
				userName = jwtUtil.extractUsername(token);
			}

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);

				if (jwtUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				} else {

				}
			}
		} catch (SignatureException ex) {
			System.out.println("Invalid JWT Signature");
		} catch (MalformedJwtException ex) {
			System.out.println("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			System.out.println("Expired JWT token");
			httpServletRequest.setAttribute("expired", ex.getMessage());
		} catch (UnsupportedJwtException ex) {
			System.out.println("Unsupported JWT exception");
		} catch (IllegalArgumentException ex) {
			System.out.println("Jwt claims string is empty");
		} catch (Exception e) {
			System.out.println("throwUnauthorized-------->" + e);
			throwUnauthorized(httpServletResponse);
		} finally {
			httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
			httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
			httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
			httpServletResponse.setHeader("Access-Control-Allow-Credentials", "*");
			httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private void throwUnauthorized(HttpServletResponse res) throws IOException {

		HttpServletResponse response = (HttpServletResponse) res;

		response.reset();
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

	}

}
