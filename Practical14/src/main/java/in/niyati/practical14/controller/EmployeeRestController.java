package in.niyati.practical14.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical14.entity.Employee;
import in.niyati.practical14.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // POST - Create an employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee saved = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201
    }

    // GET - Fetch all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees()); // 200
    }

    // GET - Fetch one employee by id.
    // No try/catch needed here anymore! If the service throws ResourceNotFoundException,
    // it automatically bubbles up to GlobalExceptionHandler.handleNotFound() -
    // this controller method doesn't need to know or care about that.
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee); // 200
    }

    // PUT - Update an employee. Also routes through ResourceNotFoundException
    // via the service layer if the id doesn't exist.
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        Employee saved = employeeService.updateEmployee(id, updatedEmployee);
        return ResponseEntity.ok(saved); // 200
    }

    // DELETE - Remove an employee. Same pattern - no manual existsById check needed here,
    // the service layer already handles the not-found case.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build(); // 204
    }
}