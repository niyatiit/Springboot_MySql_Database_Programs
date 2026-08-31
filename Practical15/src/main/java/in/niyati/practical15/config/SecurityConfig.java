package in.niyati.practical15.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // BCryptPasswordEncoder - a one-way hashing algorithm used to securely
    // store and compare passwords. Never store plain text passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Defines an in-memory user (no database needed for this practical).
    // The password is BCrypt-encoded using the passwordEncoder bean above.
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("password123"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Defines WHICH endpoints require authentication and HOW authentication happens.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disabled for simplicity in this REST API practical
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()   // no auth needed
                        .requestMatchers("/api/secure/**").authenticated() // auth required
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {}); // enables HTTP Basic Authentication

        return http.build();
    }
}