package com.ispan.theater.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.ispan.theater.util.JsonWebTokenUtility;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
	@Autowired
    private UserDetailsService userDetailsService;
	@Autowired
	@Qualifier("handlerExceptionResolver")
	private HandlerExceptionResolver resolver;
	
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    	   try {
    	        //從request獲取JWT token
    	        String token = getTokenFromRequest(request);
    	        System.out.println("filter1:"+token);
    	        //從token獲取username
    	        String temp=jsonWebTokenUtility.validateToken(token);
    	        //校验token
    	        if(StringUtils.hasText(token) && temp!=null){
    	            String username = temp.substring(temp.indexOf("\"email\":\"")+9,temp.indexOf("\"",temp.indexOf("\"email\":\"")+9));
    	            System.out.println("filter2:"+username);
    	            // 加载與　token 關聯的用户
    	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    	            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    	            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    	            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    	        }
    	        filterChain.doFilter(request, response);
    	    } catch (Exception ex) {
    	    	resolver.resolveException(request, response, null, new ExpiredJwtException(null,null,"token過期!"));
    	    }

    }

    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        System.out.println("filter3:"+bearerToken);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
    
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getRequestURI().equals("/user/login")||request.getRequestURI().startsWith("/movie/findMovie")
				||request.getRequestURI().startsWith("/movie/findAllCinema")||request.getRequestURI().startsWith("/movie/dates")
				||request.getRequestURI().startsWith("/movie/times")||request.getRequestURI().startsWith("/order-redirect")
				||request.getRequestURI().startsWith("/movie/linePayConfirm")||request.getRequestURI().startsWith("/movie/ecPayConfirm")
				||request.getRequestURI().startsWith("/user/finduserphoto")||request.getRequestURI().startsWith("/movie/tickets");
	}
    
    
}
