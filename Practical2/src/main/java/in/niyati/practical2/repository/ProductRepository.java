package in.niyati.practical2.repository;

import in.niyati.practical2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository

// STAGE 2: PagingAndSortingRepository
// Newly available methods here (in addition to everything from CrudRepository):
// findAll(Sort sort) -> get all records sorted by given field(s)
// findAll(Pageable pageable) -> get records in pages (with page number, size)

//public interface ProductRepository extends PagingAndSortingRepository<Product , Integer>, CrudRepository<Product , Integer> {
//
//}


// STAGE 3: JpaRepository
// Newly available methods here (in addition to everything from CrudRepository
// and PagingAndSortingRepository, since JpaRepository extends both):
// saveAll(Iterable<S> entities) -> batch insert/update multiple entities at once
// flush() -> immediately synchronize the persistence context to the database
// saveAndFlush(S entity) -> save a single entity and flush immediately
// deleteInBatch() / deleteAllInBatch() -> batch delete (single SQL DELETE, more efficient)
// getReferenceById(ID id) -> get a lazy reference without hitting DB immediately
public interface ProductRepository extends JpaRepository<Product,Integer> {
}
