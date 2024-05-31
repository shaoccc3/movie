package com.ispan.theater.Filter;

import com.ispan.theater.domain.Admin;
import com.ispan.theater.service.AdminService;
import com.ispan.theater.util.JsonWebTokenUtility;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JsonWebTokenUtility jwtUtility;
    private final AdminService adminService;
    public JwtAuthenticationFilter(JsonWebTokenUtility jwtUtility, AdminService adminService) {
        this.jwtUtility = jwtUtility;
        this.adminService = adminService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, java.io.IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String temp = jwtUtility.validateToken(token);

            if (temp != null && !jwtUtility.validateTime(token)) {
                String username = temp.substring(temp.indexOf("\"email\":\"") + 9, temp.indexOf("\"", temp.indexOf("\"email\":\"") + 9));
                System.out.println(username);
                if ("admin".equals(jwtUtility.extractRole(token))) {
                    UserDetails userDetails = adminService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}




