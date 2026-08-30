package com.example.Practical1.repository;

import com.example.Practical1.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee , Integer> {
    // CrudRepository already gives us:
    // save(), findAll(), findById(), deleteById(), etc.
    // No need to write anything extra for this practical.
}
