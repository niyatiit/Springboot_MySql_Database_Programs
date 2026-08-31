package in.niyati.practical16.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical16.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    // Derived query method - used by CustomUserDetailsService to look up
    // a user during login, and by AuthController to check for duplicate usernames.
    Optional<AppUser> findByUsername(String username);
}