package in.niyati.practical10.service;

import java.util.List;

import in.niyati.practical10.entity.Product;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(int id);
    Product updateProduct(int id, Product updatedProduct);
    void deleteProduct(int id);
}