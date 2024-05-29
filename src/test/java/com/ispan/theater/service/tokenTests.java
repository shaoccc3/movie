package com.ispan.theater.service;

import com.ispan.theater.util.JsonWebTokenUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class tokenTests {
    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;
    @Test
    public void testLToken() {
        String token = jsonWebTokenUtility.longEncryptToken("admin");
        System.out.println(token);
    }
}
