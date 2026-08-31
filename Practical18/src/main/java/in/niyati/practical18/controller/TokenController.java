package in.niyati.practical18.controller;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;

import in.niyati.practical18.security.JwtConstants;

@RestController
public class TokenController {

    // GET - Generates a sample JWT for TESTING purposes only.
    // In a real setup, an actual Authorization Server would issue this token
    // after a proper login flow - we're simulating that here so we have
    // something valid to test our Resource Server with.
    // Example: /generate-token?username=rahul&scope=read
    @GetMapping("/generate-token")
    public String generateToken(
            @RequestParam(defaultValue = "testuser") String username,
            @RequestParam(defaultValue = "read") String scope) {

        Key key = new SecretKeySpec(
                Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(JwtConstants.SECRET_KEY.getBytes())),
                "HmacSHA256"
        );

        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", scope);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // valid 1 hour
                .signWith(key)
                .compact();
    }
}