package in.niyati.practical13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical13.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}