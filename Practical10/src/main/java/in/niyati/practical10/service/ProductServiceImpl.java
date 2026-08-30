package in.niyati.practical10.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import in.niyati.practical10.entity.Product;
import in.niyati.practical10.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    @Override
    public Product updateProduct(int id, Product updatedProduct) {
        // Business logic lives HERE, in the service layer - not in the controller,
        // and not in the repository. The controller just calls this method;
        // it has no idea HOW the update is implemented.
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));

        if (updatedProduct.getProductName() != null) {
            existing.setProductName(updatedProduct.getProductName());
        }
        if (updatedProduct.getCategory() != null) {
            existing.setCategory(updatedProduct.getCategory());
        }
        existing.setPrice(updatedProduct.getPrice());
        existing.setQuantity(updatedProduct.getQuantity());

        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}