package microservices.book.multiplication.repository;

import microservices.book.multiplication.domain.Multiplication;
import org.springframework.data.repository.CrudRepository;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

    Multiplication findOneByFactorAAndFactorB(final int factorA, final int factorB);
}
