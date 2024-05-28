package com.ispan.theater.util;

import com.ispan.theater.domain.Admin;
import com.ispan.theater.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.ispan.theater.config.*;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class Dinit implements CommandLineRunner {

    private final PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AdminRepository adminRepository;

    public Dinit(PasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {

//        String username = "aaa";
//        String rawPassword = "aaa";
//        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
//
//        Admin admin = new Admin();
//        admin.setAdminname(username);
//        admin.setPassword(encodedPassword);
//        adminRepository.save(admin);
//        System.out.println("Inserted admin user with encoded password.");
    }

}