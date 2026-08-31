package in.niyati.practical13.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical13.entity.Customer;
import in.niyati.practical13.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    private CustomerRepository customerRepository;

    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // POST - @Valid triggers Bean Validation on the incoming Customer JSON
    // BEFORE this method body even runs. If any field violates its constraint
    // (@NotBlank, @Email, @Min), Spring automatically returns 400 Bad Request
    // without ever executing the code below.
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer saved = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201
    }

    // GET - Fetch all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerRepository.findAll()); // 200
    }

    // GET - Fetch one customer by id
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PUT - @Valid applied here too, so updates are validated the same way as creation.
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @Valid @RequestBody Customer updatedCustomer) {
        Optional<Customer> existing = customerRepository.findById(id);

        if (existing.isPresent()) {
            Customer customer = existing.get();
            customer.setName(updatedCustomer.getName());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setAge(updatedCustomer.getAge());
            Customer saved = customerRepository.save(customer);
            return ResponseEntity.ok(saved); // 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // DELETE - Remove a customer by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}