package microservices.book.multiplication.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity(name = "usuario")
public final class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    private final String alias;

    public User(String alias) {
        this.id = null;
        this.alias = alias;
    }

    protected User() {
        this.id = null;
        this.alias = null;
    }
}
