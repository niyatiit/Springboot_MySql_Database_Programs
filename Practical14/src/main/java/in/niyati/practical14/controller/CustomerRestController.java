package in.niyati.practical14.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical14.entity.Customer;
import in.niyati.practical14.exception.ResourceNotFoundException;
import in.niyati.practical14.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    private CustomerRepository customerRepository;

    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // POST - @Valid triggers validation; failures are caught by
    // GlobalExceptionHandler.handleValidation() automatically.
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

    // GET - Fetch one customer by id. Throws ResourceNotFoundException
    // (caught by GlobalExceptionHandler) instead of returning 404 manually.
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return ResponseEntity.ok(customer); // 200
    }

    // PUT - Validated + routes through ResourceNotFoundException if id doesn't exist.
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @Valid @RequestBody Customer updatedCustomer) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setName(updatedCustomer.getName());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setAge(updatedCustomer.getAge());

        Customer saved = customerRepository.save(customer);
        return ResponseEntity.ok(saved); // 200
    }

    // DELETE - Remove a customer, throwing ResourceNotFoundException if absent.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}