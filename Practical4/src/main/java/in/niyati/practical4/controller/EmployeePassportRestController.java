package in.niyati.practical4.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical4.entity.Employee;
import in.niyati.practical4.entity.Passport;
import in.niyati.practical4.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/employees-passport")
public class EmployeePassportRestController {

    private EmployeeRepository employeeRepository;

    public EmployeePassportRestController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // POST - Create Employee with nested Passport (cascade saves both)
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // GET - Fetch all employees (with nested passport)
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // GET - Fetch one employee by id
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PUT - Update employee + nested passport
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        Optional<Employee> existing = employeeRepository.findById(id);

        if (existing.isPresent()) {
            Employee employee = existing.get();
            employee.setEmpName(updatedEmployee.getEmpName());
            employee.setDepartment(updatedEmployee.getDepartment());

            if (updatedEmployee.getPassport() != null) {
                Passport existingPassport = employee.getPassport();
                Passport newPassportData = updatedEmployee.getPassport();

                if (existingPassport != null) {
                    existingPassport.setPassportNumber(newPassportData.getPassportNumber());
                    existingPassport.setIssuingCountry(newPassportData.getIssuingCountry());
                    existingPassport.setExpiryDate(newPassportData.getExpiryDate());
                } else {
                    employee.setPassport(newPassportData);
                }
            }

            Employee saved = employeeRepository.save(employee);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE - Remove employee (cascades and removes passport too)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.ok("Employee with id " + id + " (and their passport) deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee with id " + id + " not found.");
        }
    }
}