package in.niyati.practical15.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical15.model.Note;

@RestController
@RequestMapping("/api/secure")
public class SecureRestController {

    // In-memory Note store - every endpoint here requires authentication,
    // enforced by the SAME SecurityFilterChain rule ("/api/secure/**").
    private List<Note> noteList = new ArrayList<>();
    private AtomicInteger idCounter = new AtomicInteger(1);

    // GET - Fetch secured data (requires authentication)
    @GetMapping("/data")
    public String getSecureData() {
        return "This is secure data - authentication required.";
    }

    // GET - Fetch all notes
    @GetMapping("/notes")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(noteList); // 200
    }

    // POST - Create a new note
    @PostMapping("/data")
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        note.setId(idCounter.getAndIncrement());
        noteList.add(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(note); // 201
    }

    // PUT - Update an existing note
    @PutMapping("/data/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable int id, @RequestBody Note updatedNote) {
        for (Note note : noteList) {
            if (note.getId() == id) {
                note.setTitle(updatedNote.getTitle());
                note.setContent(updatedNote.getContent());
                return ResponseEntity.ok(note); // 200
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }

    // DELETE - Remove a note
    @DeleteMapping("/data/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable int id) {
        boolean removed = noteList.removeIf(note -> note.getId() == id);
        if (removed) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}