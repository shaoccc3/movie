package com.ispan.theater.service;

import com.ispan.theater.domain.Admin;
import com.ispan.theater.dto.UserCredentials;
import com.ispan.theater.repository.AdminRepository;
import com.ispan.theater.util.JsonWebTokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
@Service
public class AdminService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminname(username);
        if (admin == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        System.out.println(admin.getPassword());
        return admin;
    }
    public String login(UserCredentials userCredentials) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.username, userCredentials.password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jsonWebTokenUtility.createEncryptedToken(authentication.getName(),null);
    }
}
