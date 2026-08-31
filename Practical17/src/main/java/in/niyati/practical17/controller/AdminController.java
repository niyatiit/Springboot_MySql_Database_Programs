package in.niyati.practical17.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    // In-memory resource - a simple admin-managed store, matching your
    // assignment's "manage a simple in-memory admin-only resource" requirement.
    private List<String> adminData = new ArrayList<>();
    private AtomicInteger idCounter = new AtomicInteger(1);

    // GET - Only users with ROLE_ADMIN can access this
    @GetMapping("/api/admin/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getAdminData() {
        return ResponseEntity.ok(adminData); // 200
    }

    // GET - Accessible to BOTH ADMIN and USER roles
    @GetMapping("/api/user/data")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> getUserData() {
        return ResponseEntity.ok("This data is accessible to both ADMIN and USER roles."); // 200
    }

    // POST - Only ADMIN can add data
    @PostMapping("/api/admin/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createAdminData(@RequestBody String data) {
        String entry = idCounter.getAndIncrement() + ": " + data;
        adminData.add(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry); // 201
    }

    // PUT - Only ADMIN can update data
    @PutMapping("/api/admin/data/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAdminData(@PathVariable int id, @RequestBody String updatedData) {
        for (int i = 0; i < adminData.size(); i++) {
            if (adminData.get(i).startsWith(id + ":")) {
                String updatedEntry = id + ": " + updatedData;
                adminData.set(i, updatedEntry);
                return ResponseEntity.ok(updatedEntry); // 200
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }

    // DELETE - Only ADMIN can delete data
    @DeleteMapping("/api/admin/data/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdminData(@PathVariable int id) {
        boolean removed = adminData.removeIf(entry -> entry.startsWith(id + ":"));
        if (removed) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}