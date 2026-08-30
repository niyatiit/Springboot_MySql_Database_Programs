package in.niyati.practical10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical10.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}