package in.niyati.practical8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical8.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // JpaRepository already includes findAll(Pageable pageable) -
    // no extra method needed for pagination/sorting support.
}