package in.niyati.practical18.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure")
public class SecureDataController {

    private List<String> secureData = new ArrayList<>();
    private AtomicInteger idCounter = new AtomicInteger(1);

    // GET - requires a valid JWT AND the 'read' scope specifically.
    // hasAuthority('SCOPE_read') checks the "scope" claim we put in our test token.
    @GetMapping("/data")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public ResponseEntity<List<String>> getSecureData() {
        return ResponseEntity.ok(secureData); // 200
    }

    // POST - requires a valid JWT with 'write' scope (mutating verb)
    @PostMapping("/data")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    public ResponseEntity<String> createSecureData(@RequestBody String data) {
        String entry = idCounter.getAndIncrement() + ": " + data;
        secureData.add(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry); // 201
    }

    // PUT - requires 'write' scope
    @PutMapping("/data/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    public ResponseEntity<String> updateSecureData(@PathVariable int id, @RequestBody String updatedData) {
        for (int i = 0; i < secureData.size(); i++) {
            if (secureData.get(i).startsWith(id + ":")) {
                String updatedEntry = id + ": " + updatedData;
                secureData.set(i, updatedEntry);
                return ResponseEntity.ok(updatedEntry); // 200
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }

    // DELETE - requires 'write' scope
    @DeleteMapping("/data/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write')")
    public ResponseEntity<Void> deleteSecureData(@PathVariable int id) {
        boolean removed = secureData.removeIf(entry -> entry.startsWith(id + ":"));
        if (removed) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}