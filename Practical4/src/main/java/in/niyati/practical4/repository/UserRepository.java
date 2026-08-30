package in.niyati.practical4.repository;

import in.niyati.practical4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // No extra methods needed - JpaRepository gives us everything
    // required for full CRUD (save, findAll, findById, deleteById, etc.)
}
