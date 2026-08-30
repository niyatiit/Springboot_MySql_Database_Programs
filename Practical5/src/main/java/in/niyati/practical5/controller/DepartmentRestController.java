package in.niyati.practical5.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical5.entity.Department;
import in.niyati.practical5.entity.Employee;
import in.niyati.practical5.repository.DepartmentRepository;

@RestController
@RequestMapping("/api/departments")
public class DepartmentRestController {

    private DepartmentRepository departmentRepository;

    public DepartmentRestController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // POST - Create a Department with a list of Employees in one request
    // IMPORTANT: We must set department on EACH employee before saving,
    // since Employee is the owning side (holds the dept_id foreign key).
    // Without this, dept_id would be saved as null for every employee.
    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        if (department.getEmployees() != null) {
            for (Employee emp : department.getEmployees()) {
                emp.setDepartment(department);
            }
        }
        return departmentRepository.save(department);
    }

    // GET - Fetch all departments (with their employees)
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // GET - Fetch one department with its employees
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET - Fetch ONLY the employee list of a department
    @GetMapping("/{id}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable int id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            return ResponseEntity.ok(department.get().getEmployees());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // PUT - Rename a department
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable int id, @RequestBody Department updatedDept) {
        Optional<Department> existing = departmentRepository.findById(id);
        if (existing.isPresent()) {
            Department department = existing.get();
            department.setDeptName(updatedDept.getDeptName());
            Department saved = departmentRepository.save(department);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // PUT - Update a specific employee's details within a department
    // (Many-to-One navigation: find the employee inside the department's list)
    @PutMapping("/{deptId}/employees/{empId}")
    public ResponseEntity<Employee> updateEmployeeInDepartment(
            @PathVariable int deptId, @PathVariable int empId, @RequestBody Employee updatedEmp) {

        Optional<Department> deptOpt = departmentRepository.findById(deptId);
        if (deptOpt.isPresent()) {
            Department department = deptOpt.get();
            for (Employee emp : department.getEmployees()) {
                if (emp.getEmpId() == empId) {
                    emp.setEmpName(updatedEmp.getEmpName());
                    emp.setSalary(updatedEmp.getSalary());
                    departmentRepository.save(department);
                    return ResponseEntity.ok(emp);
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // employee not found in this department
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // department not found
        }
    }

    // DELETE - Remove a single employee from a department (Many-to-One side)
    @DeleteMapping("/{deptId}/employees/{empId}")
    public ResponseEntity<String> deleteEmployeeFromDepartment(
            @PathVariable int deptId, @PathVariable int empId) {

        Optional<Department> deptOpt = departmentRepository.findById(deptId);
        if (deptOpt.isPresent()) {
            Department department = deptOpt.get();
            boolean removed = department.getEmployees().removeIf(emp -> emp.getEmpId() == empId);
            if (removed) {
                departmentRepository.save(department);
                return ResponseEntity.ok("Employee with id " + empId + " removed from department " + deptId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found in this department.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department not found.");
        }
    }

    // DELETE - Remove a department along with all its employees (cascade, One-to-Many side)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable int id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return ResponseEntity.ok("Department with id " + id + " (and all its employees) deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department with id " + id + " not found.");
        }
    }
}