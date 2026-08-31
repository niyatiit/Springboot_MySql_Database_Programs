package in.niyati.practical18.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import in.niyati.practical18.security.JwtConstants;

@Configuration
@EnableMethodSecurity
public class ResourceServerConfig {

    // JwtDecoder - tells Spring Security HOW to verify incoming JWTs.
    // Here we use the SAME shared secret to verify the signature
    // (HMAC/HS256). In production with a real Authorization Server,
    // you'd instead configure spring.security.oauth2.resourceserver.jwt.issuer-uri
    // in application.properties, and Spring would auto-fetch the public key -
    // no manual JwtDecoder bean needed at all.
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec key = new SecretKeySpec(JwtConstants.SECRET_KEY.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/generate-token").permitAll() // our test token generator stays open
                        .requestMatchers("/api/secure/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));

        return http.build();
    }
}