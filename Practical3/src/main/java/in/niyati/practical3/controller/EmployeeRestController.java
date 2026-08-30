package in.niyati.practical3.controller;

import in.niyati.practical3.entity.Employee;
import in.niyati.practical3.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private EmployeeRepository employeeRepository;

    public EmployeeRestController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // post - create and save a new employee (used to seed sample data)
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    //post - Batch seed multiple employee at once
    @PostMapping("/batch")
    public List<Employee> saveMultipleEmployee(@RequestBody List<Employee> employees) {
        return employeeRepository.saveAll(employees);
    }

    @GetMapping()
    public List<Employee> getAllEmployee(Employee employee){
        return employeeRepository.findAll();
    }

    //get - fetch one employee id only
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    //get - Derived query : employee by department
    //ex:- /api/employees/department/IT
    @GetMapping("/department/{department}")
    public List<Employee> getEmployeeByDepartment(@PathVariable String department) {
        return employeeRepository.findByDepartment(department);
    }

    //    get - Derived query : Count of employee in a department
//    ex :- /api/employee/department/IT/count
    @GetMapping("/department/{department}/count")
    public long getEmployeeCountByDepartment(@PathVariable String department) {
        return employeeRepository.countByDepartment(department);
    }


    // GET - Derived query: employees earning more than a given salary
    // Example: /api/employees/salary/greater?salary=50000

    @GetMapping("/salary/greater")
    public List<Employee> gtEmployeeBySalaryGreaterThan(@RequestParam double salary){
        return employeeRepository.findBySalaryGreaterThan(salary);
    }

    // GET - Custom JPQL query: employees within a salary range
    // Example: /api/employees/salary/range?min=30000&max=60000
    @GetMapping("/salary/range")
        public List<Employee> getEmployeeBySalaryRange(@RequestParam double min , @RequestParam double max){
            return employeeRepository.findBySalaryRange(min,max);
        }


    // PUT - Update an employee's details (e.g. revise salary)
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody Employee updateEmployee){
        Optional<Employee> existing = employeeRepository.findById(id);

        if(existing.isPresent()){
            Employee employee = existing.get();

            employee.setEmpName((updateEmployee.getEmpName()));
            employee.setDepartment((updateEmployee.getDepartment()));
            employee.setSalary((updateEmployee.getSalary()));


            Employee saved = employeeRepository.save(employee);
            return ResponseEntity.ok(saved);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE - Remove an employee by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id){
        if(employeeRepository.existsById(id)){

            employeeRepository.deleteById(id);
            return ResponseEntity.ok("Employee with id : " + id + " has been successfully deleted");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
