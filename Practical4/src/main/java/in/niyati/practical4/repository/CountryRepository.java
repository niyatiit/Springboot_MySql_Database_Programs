package in.niyati.practical4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.niyati.practical4.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
}