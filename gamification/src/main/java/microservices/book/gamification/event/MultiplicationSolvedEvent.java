package microservices.book.gamification.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Event that models the fact that a {@link microservices.book.gamification.domain.Multiplication}
 * has been solved in the system. Provides some context
 information about the multiplication.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class MultiplicationSolvedEvent {
    private final Long multiplicationResultAttempt;
    private final Long userId;
    private final boolean correct;

    public MultiplicationSolvedEvent() {
        this.multiplicationResultAttempt = 0L;
        this.userId = 0L;
        this.correct = false;
    }
}
