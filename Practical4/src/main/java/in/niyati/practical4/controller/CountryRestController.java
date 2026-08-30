package in.niyati.practical4.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.niyati.practical4.entity.Country;
import in.niyati.practical4.entity.Capital;
import in.niyati.practical4.repository.CountryRepository;

@RestController
@RequestMapping("/api/countries")
public class CountryRestController {

    private CountryRepository countryRepository;

    public CountryRestController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    // POST - Create Country with nested Capital (cascade saves both)
    @PostMapping
    public Country createCountry(@RequestBody Country country) {
        return countryRepository.save(country);
    }

    // GET - Fetch all countries (with nested capital)
    @GetMapping
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    // GET - Fetch one country by id
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable int id) {
        Optional<Country> country = countryRepository.findById(id);
        return country.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PUT - Update country + nested capital
    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable int id, @RequestBody Country updatedCountry) {
        Optional<Country> existing = countryRepository.findById(id);

        if (existing.isPresent()) {
            Country country = existing.get();
            country.setCountryName(updatedCountry.getCountryName());

            if (updatedCountry.getCapital() != null) {
                Capital existingCapital = country.getCapital();
                Capital newCapitalData = updatedCountry.getCapital();

                if (existingCapital != null) {
                    existingCapital.setCityName(newCapitalData.getCityName());
                    existingCapital.setPopulation(newCapitalData.getPopulation());
                } else {
                    country.setCapital(newCapitalData);
                }
            }

            Country saved = countryRepository.save(country);
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE - Remove country (cascades and removes capital too)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable int id) {
        if (countryRepository.existsById(id)) {
            countryRepository.deleteById(id);
            return ResponseEntity.ok("Country with id " + id + " (and its capital) deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Country with id " + id + " not found.");
        }
    }
}