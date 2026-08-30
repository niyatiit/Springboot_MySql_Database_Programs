package in.niyati.practical3.repository;

import in.niyati.practical3.entity.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee , Integer> {

    // DERIVED QUERY METHOD 1
    // Spring Data parses the method name and generates:
    // SELECT e FROM Employee e WHERE e.department = ?1
    List<Employee> findByDepartment(String department);

    // DERIVED QUERY METHOD 2
    // Generates: SELECT COUNT(e) FROM Employee e WHERE e.department = ?1
    long countByDepartment(String department);

    // DERIVED QUERY METHOD 3
    // Generates: SELECT e FROM Employee e WHERE e.salary > ?1
    List<Employee> findBySalaryGreaterThan(double salary);

    // CUSTOM JPQL QUERY using @Query with named parameters
    // Used when the requirement can't be expressed by method-name conventions alone
    @Query("SELECT e from Employee e WHERE e.salary BETWEEN :min AND :max")
    List<Employee> findBySalaryRange(@Param("min") double min , @Param("max") double max);

}
