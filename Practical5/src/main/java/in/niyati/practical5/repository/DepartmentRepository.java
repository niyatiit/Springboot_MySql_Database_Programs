package in.niyati.practical5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical5.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}