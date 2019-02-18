package microservices.book.gamification.domain;

/**
 * Enumeração contendo todas as medalhas
 */
public enum Badge {
    // Badges depending on score
    BRONZE_MULTIPLICATOR,
    SILVER_MULTIPLICATOR,
    GOLD_MULTIPLICATOR,
    // Other badges won for different conditions
    FIRST_ATTEMPT,
    FIRST_WON,
    LUCKY_NUMBER
}
