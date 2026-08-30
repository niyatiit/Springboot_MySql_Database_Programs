package com.example.Practical1.controller;

import com.example.Practical1.entity.Employee;
import com.example.Practical1.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    //Post :- save a new employee
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }


    //get :- Fetch all employee
    @GetMapping
    public List<Employee> getAllEmployee() {
        return (List<Employee>) employeeRepository.findAll();
    }


    // get :- Fetch single employee
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id){
        Optional<Employee> employee =  employeeRepository.findById(id);

        if(employee.isPresent()){
            return ResponseEntity.ok(employee.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    //Put :- Update single employee
    @PutMapping("/{id}")
    public  ResponseEntity<Employee> updateEmployee(@PathVariable int id , @RequestBody Employee updatedEmployee){
        Optional<Employee> existingEmployee =  employeeRepository.findById(id);

        if(existingEmployee.isPresent()){
            Employee emp = existingEmployee.get();

            emp.setEmpName(updatedEmployee.getEmpName());
            emp.setDepartment(updatedEmployee.getDepartment());
            emp.setSalary(updatedEmployee.getSalary());

            Employee saved = employeeRepository.save(emp);

            return ResponseEntity.ok(saved);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Delete :- Remove the employee by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletedEmployee(@PathVariable int id){
        if(employeeRepository.existsById(id)){
            employeeRepository.deleteById(id);
            return ResponseEntity.ok(("Employee with id : " + id + " deleted Successfully"));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee with id " + id + " not found.");
        }
    }
}
