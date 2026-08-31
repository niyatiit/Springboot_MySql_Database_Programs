package in.niyati.practical16.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical16.entity.AppUser;
import in.niyati.practical16.repository.AppUserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // POST - Register a new user. Encodes the plain-text password BEFORE saving -
    // this is the exact requirement from your assignment step 4.
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AppUser appUser) {
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword())); // encode BEFORE saving
        if (appUser.getRole() == null || appUser.getRole().isEmpty()) {
            appUser.setRole("USER"); // default role if not provided
        }

        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully."); // 201
    }

    // PUT - Update the AUTHENTICATED user's own username/role.
    // "Authentication" here is injected by Spring Security - it tells us
    // WHO is currently logged in (from the Basic Auth credentials sent).
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody AppUser updatedInfo, Authentication authentication) {
        String currentUsername = authentication.getName();

        Optional<AppUser> userOpt = appUserRepository.findByUsername(currentUsername);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        AppUser user = userOpt.get();
        if (updatedInfo.getUsername() != null && !updatedInfo.getUsername().isEmpty()) {
            user.setUsername(updatedInfo.getUsername());
        }
        if (updatedInfo.getRole() != null && !updatedInfo.getRole().isEmpty()) {
            user.setRole(updatedInfo.getRole());
        }

        appUserRepository.save(user);
        return ResponseEntity.ok("Profile updated successfully."); // 200
    }

    // DELETE - Remove the AUTHENTICATED user's own account.
    @DeleteMapping("/deregister")
    public ResponseEntity<String> deregister(Authentication authentication) {
        String currentUsername = authentication.getName();

        Optional<AppUser> userOpt = appUserRepository.findByUsername(currentUsername);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        appUserRepository.delete(userOpt.get());
        return ResponseEntity.noContent().build(); // 204
    }
}