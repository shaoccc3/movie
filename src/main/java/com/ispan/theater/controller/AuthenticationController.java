package com.ispan.theater.controller;

import com.ispan.theater.DTO.JWTAuthResponse;
import com.ispan.theater.DTO.UserCredentials;
import com.ispan.theater.domain.Admin;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.AdminRepository;
import com.ispan.theater.service.AdminService;
import com.ispan.theater.util.JsonWebTokenUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/api")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserCredentials credentials) {
        String token = adminService.login(credentials);
        System.out.println("admin");
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse(token);
        System.out.println(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }
//    public ResponseEntity<?> adminLogin(@RequestBody UserCredentials credentials) {
//        Admin admin = adminRepository.findByAdminname(credentials.username);
//        if (admin != null) {
//            //----security----
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(),admin.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            //----security----
//            JSONObject inputjson = new JSONObject().put("userid", admin.getId()).put("email", admin.getUsername());
//            String token = jsonWebTokenUtility.adminToken(inputjson.toString(), null);
//            return ResponseEntity.ok(new JWTAuthResponse(token));
//        } else {
//            return ResponseEntity.status(403).body("Forbidden");
//        }
//    }
}
