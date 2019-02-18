package microservices.book.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class BadgeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BADGE_ID")
    private final Long badgeId;
    private final Long userId;
    private final long badgeTimestamp;
    private final Badge badge;

    // Empty constructor for JSON / JPA
    public BadgeCard() {
        this(null, null, 0, null);
    }

    public BadgeCard(final Long userId, final Badge badge) {
        this(null, userId, System.currentTimeMillis(), badge);
    }

}
