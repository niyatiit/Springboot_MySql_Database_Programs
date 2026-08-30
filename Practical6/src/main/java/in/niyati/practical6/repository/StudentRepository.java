package in.niyati.practical6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical6.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}