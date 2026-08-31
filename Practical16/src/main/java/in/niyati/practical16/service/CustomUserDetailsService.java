package in.niyati.practical16.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.niyati.practical16.entity.AppUser;
import in.niyati.practical16.repository.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    // Spring Security calls this method AUTOMATICALLY whenever someone tries
    // to authenticate (e.g., via HTTP Basic Auth). We look up the user in our
    // database by username, then build a Spring Security User object using
    // the ALREADY-ENCODED password stored in the DB (never re-encode it here -
    // Spring Security compares the incoming plain password against this hash internally).
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(appUser.getUsername())
                .password(appUser.getPassword()) // already BCrypt-encoded in the DB
                .roles(appUser.getRole())
                .build();
    }
}