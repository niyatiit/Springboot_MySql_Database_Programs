package in.niyati.practical4.controller;

import in.niyati.practical4.entity.User;
import in.niyati.practical4.entity.UserProfile;
import in.niyati.practical4.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //    Post :- create a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    //    Get all
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //    Get by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //    Update
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        Optional<User> existing = userRepository.findById(id);

        if (existing.isPresent()) {
            User user = existing.get();

            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());

//        Update nested profile fields too , if profile data was sent

            if (updatedUser.getUserProfile() != null) {
                UserProfile existingProfile = user.getUserProfile();
                UserProfile newProfileData = updatedUser.getUserProfile();

                if (existingProfile != null) {
                    existingProfile.setAddress(newProfileData.getAddress());
                    existingProfile.setPhone(newProfileData.getPhone());
                    existingProfile.setDob(newProfileData.getDob());
                } else {
                    user.setUserProfile(newProfileData);
                }
            }
            User saved = userRepository.save(user);
            return ResponseEntity.ok(saved);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User with id " + id + " (and their profile) deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " not found.");
        }
    }

}
