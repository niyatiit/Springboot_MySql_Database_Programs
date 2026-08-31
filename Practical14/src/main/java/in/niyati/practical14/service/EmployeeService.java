package in.niyati.practical14.service;

import java.util.List;

import in.niyati.practical14.entity.Employee;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(int id);
    Employee updateEmployee(int id, Employee updatedEmployee);
    void deleteEmployee(int id);
}