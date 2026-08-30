package in.niyati.practical11.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical11.entity.Customer;
import in.niyati.practical11.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    // SLF4J logger - standard way to log in Spring Boot apps (better than System.out
    // since it supports log levels, timestamps, and can be configured/redirected).
    private static final Logger logger = LoggerFactory.getLogger(CustomerRestController.class);

    private CustomerRepository customerRepository;

    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // POST - Create a customer using @RequestBody (full JSON object in the request body)
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        logger.info("[@RequestBody] Received customer JSON in request body: {}", customer.getName());
        Customer saved = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201
    }

    // GET - Fetch all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerRepository.findAll()); // 200
    }

    // GET - Fetch one customer by id using @PathVariable (value comes from the URL path)
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable int id) {
        logger.info("[@PathVariable] Received id from URL path: {}", id);
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET - Search customers using @RequestParam (values come from the URL query string)
    // Example: /api/customers/search?city=Ahmedabad&minAge=25
    // minAge is OPTIONAL and defaults to 0 if not provided.
    @GetMapping("/search")
    public ResponseEntity<List<Customer>> search(
            @RequestParam String city,
            @RequestParam(required = false, defaultValue = "0") int minAge) {

        logger.info("[@RequestParam] Received query params -> city: {}, minAge: {}", city, minAge);
        List<Customer> results = customerRepository.findByCityAndAgeGreaterThanEqual(city, minAge);
        return ResponseEntity.ok(results); // 200
    }

    // PUT - Update a customer using @PathVariable (id) + @RequestBody (updated fields)
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer updatedCustomer) {
        logger.info("[@PathVariable + @RequestBody] Updating customer id: {} with new data", id);
        Optional<Customer> existing = customerRepository.findById(id);

        if (existing.isPresent()) {
            Customer customer = existing.get();
            if (updatedCustomer.getName() != null) {
                customer.setName(updatedCustomer.getName());
            }
            if (updatedCustomer.getCity() != null) {
                customer.setCity(updatedCustomer.getCity());
            }
            customer.setAge(updatedCustomer.getAge());
            Customer saved = customerRepository.save(customer);
            return ResponseEntity.ok(saved); // 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // DELETE - Remove a customer using @PathVariable alone
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id) {
        logger.info("[@PathVariable] Deleting customer with id: {}", id);
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}