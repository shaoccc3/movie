package com.ispan.theater.controller;

import com.ispan.theater.DTO.JWTAuthResponse;
import com.ispan.theater.DTO.UserCredentials;
import com.ispan.theater.service.AdminService;
import com.ispan.theater.util.JsonWebTokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserCredentials credentials) {
        String token = adminService.login(credentials);
        System.out.println(token);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
