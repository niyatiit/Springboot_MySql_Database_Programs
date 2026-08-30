package in.niyati.practical10.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical10.entity.Product;
import in.niyati.practical10.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    // Controller injects ONLY the service - never the repository directly.
    // This keeps persistence access fully hidden behind the service layer.
    private ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    // POST - Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product saved = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201
    }

    // GET - Fetch all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts()); // 200
    }

    // GET - Fetch one product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product); // 200
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // PUT - Update a product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        try {
            Product saved = productService.updateProduct(id, updatedProduct);
            return ResponseEntity.ok(saved); // 200
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // DELETE - Remove a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}