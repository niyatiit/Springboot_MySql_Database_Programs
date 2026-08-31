package in.niyati.practical17.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.niyati.practical17.entity.AppUser;
import in.niyati.practical17.repository.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // .roles(appUser.getRole()) automatically prefixes with "ROLE_" internally -
        // so storing "ADMIN" in the DB becomes "ROLE_ADMIN" for Spring Security's
        // hasRole('ADMIN') checks to work correctly.
        return User.withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole())
                .build();
    }
}