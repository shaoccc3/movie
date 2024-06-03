package com.ispan.theater.service;

import com.ispan.theater.DTO.UserCredentials;
import com.ispan.theater.domain.Admin;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.AdminRepository;
import com.ispan.theater.util.JsonWebTokenUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class AdminService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
    @Autowired
    public AdminService( @Lazy AuthenticationManager authenticationManager,@Lazy PasswordEncoder passwordEncode) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncode;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminname(username.trim());
        if (admin == null) {
            System.out.println("Admin not found");
            throw new UsernameNotFoundException("Admin not found with name: " + username);
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new org.springframework.security.core.userdetails.User(username,admin.getPassword(), authorities);

    }
    public String login(UserCredentials userCredentials) {
        System.out.println("login"+userCredentials.username);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        try {
            Admin admin = adminRepository.findByAdminname(userCredentials.username);
            if (admin == null) {
                throw new UsernameNotFoundException("Admin not found: " + userCredentials.username);
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCredentials.username, userCredentials.password, authorities));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            JSONObject inputjson = new JSONObject().put("userid", admin.getId()).put("email", admin.getUsername());
            return jsonWebTokenUtility.adminToken(inputjson.toString(), null);
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            throw e;
        }
    }
    private Collection<? extends GrantedAuthority> getAuthorities(Admin admin) {
        return AuthorityUtils.createAuthorityList("ADMIN");
    }
}
