package microservices.book.multiplication.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class Multiplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final int factorA;
    private final int factorB;

    public Multiplication(int factorA, int factorB) {
        this.factorA = factorA;
        this.factorB = factorB;
    }

    public Multiplication() {
        this(0, 0);
    }

    public int getResult() {
        return factorA * factorB;
    }

}
