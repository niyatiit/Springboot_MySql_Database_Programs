package in.niyati.practical17.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical17.entity.AppUser;
import in.niyati.practical17.repository.AppUserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // POST - Register a new user with a specified role (ADMIN or USER).
    // This lets us create at least one ADMIN and one USER account for this practical.
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AppUser appUser) {
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        if (appUser.getRole() == null || appUser.getRole().isEmpty()) {
            appUser.setRole("USER"); // default role if not specified
        }

        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully with role: " + appUser.getRole());
    }
}