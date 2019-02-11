package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

    /**
     * Creates a Multiplication object with two randomly-generated
     factors
     * between 11 and 99.
     *
     * @return a Multiplication object with random factors
     */
    Multiplication createRandomMultiplication();

    /**
     * @return true if the attempt matches the result of the
     * multiplication, false otherwise.
     */
    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

    /**
     *
     * @return List<MultiplicationResultAttempt> last results attempts for the user
     * multiplication, false otherwise.
     */
    List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
}
