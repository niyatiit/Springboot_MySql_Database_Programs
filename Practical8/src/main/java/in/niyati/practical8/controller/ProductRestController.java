package in.niyati.practical8.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical8.entity.Product;
import in.niyati.practical8.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // POST - Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // POST - Batch seed multiple products at once (handy for seeding 15-20 records)
    @PostMapping("/batch")
    public List<Product> createMultipleProducts(@RequestBody List<Product> products) {
        return productRepository.saveAll(products);
    }

    // GET - Fetch all products (no pagination, for reference)
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET - Paginated + sorted products
    // Example: /api/products/page?page=0&size=5&sortBy=price
    @GetMapping("/page")
    public Page<Product> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "productId") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productRepository.findAll(pageable);
    }

    // GET - Fetch one product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PUT - Update a product's details (e.g. price/category)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isPresent()) {
            Product product = existing.get();
            if (updatedProduct.getProductName() != null) {
                product.setProductName(updatedProduct.getProductName());
            }
            if (updatedProduct.getCategory() != null) {
                product.setCategory(updatedProduct.getCategory());
            }
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            Product saved = productRepository.save(product);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE - Remove a product by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}