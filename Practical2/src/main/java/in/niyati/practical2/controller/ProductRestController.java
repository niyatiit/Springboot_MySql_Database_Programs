package in.niyati.practical2.controller;

import in.niyati.practical2.entity.Product;
import in.niyati.practical2.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Create One Product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }


    //Find All
    @GetMapping
    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }


    //find by id only one product find
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);

        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    //Update Product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        Optional<Product> existing = productRepository.findById(id);

        if (existing.isPresent()) {
            Product product = existing.get();
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setPrice(updatedProduct.getPrice());
            product.setQuality(updatedProduct.getQuality());

            Product saved = productRepository.save(product);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        if(productRepository.existsById(id)){
          productRepository.deleteById(id);
          return  ResponseEntity.ok("Product with id : " + id + " is Deleted Successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/sorted")
    public List<Product> getProductsSortedByPrice(){
        return (List<Product>) productRepository.findAll(Sort.by("price"));
    }

    @PostMapping("/batch")
    public List<Product> saveMultipleProducts(@RequestBody List<Product> products){
        List<Product> savedProduct = productRepository.saveAll(products);
        productRepository.flush();
        return savedProduct;
    }
}
