package in.niyati.practical7.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical7.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
}