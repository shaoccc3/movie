package com.ispan.theater.util;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;
import jakarta.annotation.PostConstruct;

import static javax.crypto.Cipher.SECRET_KEY;
import static net.sf.jsqlparser.util.validation.metadata.NamedObject.role;

@Component
public class JsonWebTokenUtility {
    private static final Logger log = LoggerFactory.getLogger(JsonWebTokenUtility.class);
    @Value("${jwt.token.expire}")
    private long expire;
    private SecretKey secretKey;
    private byte[] base64EncodedSecret;    //用在簽章
    private char[] charArraySecret;        //用在加密

    @PostConstruct
    public void init() {

        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secret = "";

        //將密鑰使用base64編碼
        base64EncodedSecret = Base64.getEncoder().encode(secret.getBytes());
        charArraySecret = new String(base64EncodedSecret).toCharArray();

    }

    public String createEncryptedToken(String data, Long lifespan) {
        java.util.Date now = new java.util.Date();
        if (lifespan == null) {
            lifespan = this.expire * 60 * 1000;
        }
        long end = System.currentTimeMillis() + lifespan;
        java.util.Date expiredate = new java.util.Date(end);

        //建立密碼
        Password password = Keys.password(charArraySecret);
        JwtBuilder builder = Jwts.builder()
                .subject(data)                    //JWT主體內容
                .issuedAt(now)                    //建立時間
                .expiration(expiredate)            //過期時限
                .encryptWith(password,            //加密：避免內容被讀取
                        Jwts.KEY.PBES2_HS512_A256KW,
                        Jwts.ENC.A256GCM);

        String token = builder.compact();
        return token;
    }

    public String validateEncryptedToken(String token) {
        //建立密碼
        Password password = Keys.password(charArraySecret);
        JwtParser parser = Jwts.parser()
                .decryptWith(password)            //解密：以便讀取內容
                .build();
        try {
            Claims claims = parser.parseEncryptedClaims(token).getPayload();
            //取出JWT主體內容
            String subject = claims.getSubject();

            return subject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateEncryptedTime(String token) {
        //建立密碼
        Password password = Keys.password(charArraySecret);
        JwtParser parser = Jwts.parser()
                .decryptWith(password)            //解密：以便讀取內容
                .build();
        try {
            Claims claims = parser.parseEncryptedClaims(token).getPayload();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public String createToken(String data, Long lifespan) {
        java.util.Date now = new java.util.Date();
        if (lifespan == null) {
            lifespan = expire * 60 * 1000;
        }
        long end = System.currentTimeMillis() + lifespan;
        java.util.Date expiredate = new java.util.Date(end);

        //使用HMACS-SHA演算法建立簽章密鑰
        //SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);
        JwtBuilder builder = Jwts.builder()
                .subject(data)                    //JWT主體內容
                .issuedAt(now)                    //建立時間
                .expiration(expiredate)            //過期時限
                .signWith(secretKey);            //使用密鑰簽章：避免內容被竄改
        return builder.compact();
    }

    public String validateToken(String token) {
        //使用HMACS-SHA演算法建立簽章密鑰
        //SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)        //使用密鑰驗證簽章：避免內容被竄改
                .build();
        try {
            Claims claims = parser.parseSignedClaims(token).getPayload();

            //取出JWT主體內容
            return claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateTime(String token) {
        //使用HMACS-SHA演算法建立簽章密鑰
        //SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)        //使用密鑰驗證簽章：避免內容被竄改
                .build();
        try {
            Claims claims = parser.parseSignedClaims(token).getPayload();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    public String adminToken(String data,Long lifespan){
        java.util.Date now = new java.util.Date();
        if (lifespan == null) {
            lifespan = expire * 60 * 1000;
        }
        java.util.Date expiredate = new java.util.Date(System.currentTimeMillis() + lifespan);
        JwtBuilder builder = Jwts.builder()
                .subject(data)                    //JWT主體內容
                .claim("role", "admin")
                .issuedAt(now)                    //建立時間
                .expiration(expiredate)            //過期時限
                .signWith(secretKey);            //使用密鑰簽章：避免內容被竄改
        return builder.compact();
    }
    public String longToken(String data){
        java.util.Date now = new java.util.Date();
        java.util.Date expiredate = new java.util.Date(System.currentTimeMillis() + 315569520000L);
        JwtBuilder builder = Jwts.builder()
                .subject(data)                    //JWT主體內容
                .issuedAt(now)                    //建立時間
                .expiration(expiredate)            //過期時限
                .signWith(secretKey);            //使用密鑰簽章：避免內容被竄改
        return builder.compact();
    }
    public String longEncryptToken(String data) {
        java.util.Date now = new java.util.Date();
        java.util.Date expiredate = new java.util.Date(System.currentTimeMillis() + 315569520000L);
        //建立密碼
        JwtBuilder builder = Jwts.builder()
                .subject(data)                    //JWT主體內容
                .issuedAt(now)                    //建立時間
                .expiration(expiredate)            //過期時限
                .encryptWith(Keys.password(charArraySecret),            //加密：避免內容被讀取
                        Jwts.KEY.PBES2_HS512_A256KW,
                        Jwts.ENC.A256GCM);

        return builder.compact();
    }
    public Claims extractClaims(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)        //使用密鑰驗證簽章：避免內容被竄改
                .build();
        try {
            return parser.parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
