package in.niyati.practical12.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical12.dto.EmployeeDTO;
import in.niyati.practical12.entity.Employee;
import in.niyati.practical12.mapper.EmployeeMapper;
import in.niyati.practical12.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private EmployeeRepository employeeRepository;

    public EmployeeRestController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // POST - Accepts an EmployeeDTO (not the full entity), converts it to an
    // Employee entity, and saves it. Note: since DTO doesn't carry salary/password,
    // those fields stay at their default values (0.0 / null) unless set separately.
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        Employee saved = employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toDto(saved)); // 201
    }

    // GET - Fetch all employees, converting each Entity -> DTO before returning.
    // The password field never leaves the server, since EmployeeDTO doesn't have it.
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> dtoList = employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList); // 200
    }

    // GET - Fetch one employee by id, converted to DTO
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(e -> ResponseEntity.ok(EmployeeMapper.toDto(e)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PUT - Accepts an EmployeeDTO, maps it onto the EXISTING entity
    // (preserving password and salary, which the DTO never touches), then saves.
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable int id, @RequestBody EmployeeDTO employeeDTO) {
        Optional<Employee> existing = employeeRepository.findById(id);

        if (existing.isPresent()) {
            Employee employee = existing.get();
            // Only update the fields the DTO actually carries.
            // password and salary remain UNTOUCHED, since they were never part of the DTO.
            employee.setEmpName(employeeDTO.getEmpName());
            employee.setDepartment(employeeDTO.getDepartment());

            Employee saved = employeeRepository.save(employee);
            return ResponseEntity.ok(EmployeeMapper.toDto(saved)); // 200
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // DELETE - Remove an employee by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }
}