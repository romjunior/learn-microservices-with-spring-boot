package microservices.book.multiplication.repository;

import microservices.book.multiplication.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * método para encontrar usuário pelo alias
     * @param alias
     * @return User
     */
    Optional<User> findOneByAlias(final String alias);

}
