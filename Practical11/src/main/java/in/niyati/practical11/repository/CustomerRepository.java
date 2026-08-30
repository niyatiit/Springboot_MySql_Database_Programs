package in.niyati.practical11.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical11.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // Derived query method - used by the search endpoint.
    // Finds customers by city where age is greater than or equal to a minimum age.
    List<Customer> findByCityAndAgeGreaterThanEqual(String city, int minAge);
}