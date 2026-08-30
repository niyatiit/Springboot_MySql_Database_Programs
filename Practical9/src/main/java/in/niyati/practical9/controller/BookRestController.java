package in.niyati.practical9.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical9.model.Book;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    // In-memory store - a simple List acting as our "database" for this practical.
    // Data resets every time the app restarts, since nothing is persisted.
    private List<Book> bookList = new ArrayList<>();

    // Used to auto-generate ids, similar to how @GeneratedValue works with a real DB.
    private AtomicInteger idCounter = new AtomicInteger(1);

    // POST - Create a new book
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        book.setId(idCounter.getAndIncrement());
        bookList.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book); // 201
    }

    // GET - Fetch all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookList); // 200
    }

    // GET - Fetch one book by id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        for (Book book : bookList) {
            if (book.getId() == id) {
                return ResponseEntity.ok(book); // 200
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }

    // PUT - Update an existing book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
        for (Book book : bookList) {
            if (book.getId() == id) {
                if (updatedBook.getTitle() != null) {
                    book.setTitle(updatedBook.getTitle());
                }
                if (updatedBook.getAuthor() != null) {
                    book.setAuthor(updatedBook.getAuthor());
                }
                book.setPrice(updatedBook.getPrice());
                return ResponseEntity.ok(book); // 200
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }

    // DELETE - Remove a book by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        boolean removed = bookList.removeIf(book -> book.getId() == id);
        if (removed) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}