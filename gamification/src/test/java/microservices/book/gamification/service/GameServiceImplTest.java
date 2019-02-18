package microservices.book.gamification.service;

import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

public class GameServiceImplTest {

    private GameServiceImpl gameService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Mock
    private BadgeCardRepository badgeCardRepository;

    @Mock
    private MultiplicationResultAttemptClient multiplicationResultAttemptClient;

    @Before
    public void setUp() {
        //With this call to initMocks we tell Mockito to process the annotations
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(scoreCardRepository, badgeCardRepository, multiplicationResultAttemptClient);

        // Common given - attempt does not contain a lucky number by default
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 20, 70, 1400, true);
        BDDMockito.given(multiplicationResultAttemptClient.retrieveMultiplicationResultAttemptbyId(anyLong()))
                .willReturn(attempt);
    }

    @Test
    public void processFirstCorrectAttemptTest() {
        // given
        Long userId = 1L;
        Long attemptId = 8L;
        int totalScore = 10;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        // this repository will return the just-won score card
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.emptyList());


        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

        // assert - should score one card and win the badge FIRST_WON
        Assertions.assertThat(iteration.getScore()).isEqualTo(scoreCard.getScore());
        Assertions.assertThat(iteration.getBadges()).containsOnly(Badge.FIRST_WON);
    }

    @Test
    public void processCorrectAttemptForScoreBadgeTest() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        int totalScore = 100;
        BadgeCard firstWonBadge = new BadgeCard(userId, Badge.FIRST_WON);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        // this repository will return the just-won score card
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(createNScoreCards(10, userId));
        // the first won badge is already there
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(firstWonBadge));


        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

        // assert - should score one card and win the badge BRONZE
        Assertions.assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        Assertions.assertThat(iteration.getBadges()).containsOnly(Badge.BRONZE_MULTIPLICATOR);
    }

    @Test
    public void processCorrectAttemptForLuckyNumberBadgeTest() {
        //given
        Long userId = 1L;
        Long attemptId = 29L;
        int totalScore = 10;

        BadgeCard firstWonBadge = new BadgeCard(userId, Badge.FIRST_WON);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        // this repository will return the just-won score card
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(createNScoreCards(1, userId));
        // the first won badge is already there
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(firstWonBadge));
        // the attempt includes the lucky number
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 42, 10, 420, true);
        given(multiplicationResultAttemptClient.retrieveMultiplicationResultAttemptbyId(attemptId))
                .willReturn(attempt);

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);
        // assert - should score one card and win the badge LUCKY NUMBER
        Assertions.assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        Assertions.assertThat(iteration.getBadges()).containsOnly(Badge.LUCKY_NUMBER);
    }

    @Test
    public void processWrongAttemptTest() {
        // given
        Long userId = 1L;
        Long attemptId = 8L;
        int totalScore = 10;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        // this repository will return the just-won score card
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.emptyList());


        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, false);

        // assert - shouldn't score anything
        Assertions.assertThat(iteration.getScore()).isEqualTo(0);
        Assertions.assertThat(iteration.getBadges()).isEmpty();
    }

    @Test
    public void retrieveStatsForUserTest() {
        // given
        Long userId = 1L;
        int totalScore = 1000;
        BadgeCard badgeCard = new BadgeCard(userId, Badge.SILVER_MULTIPLICATOR);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(badgeCard));

        // when
        GameStats stats = gameService.retrieveStatsForUser(userId);

        // assert - should score one card and win the badge FIRST_WON
        Assertions.assertThat(stats.getScore()).isEqualTo(totalScore);
        Assertions.assertThat(stats.getBadges()).containsOnly(Badge.SILVER_MULTIPLICATOR);
    }

    private List<ScoreCard> createNScoreCards(int n, Long userId) {
        return IntStream.range(0, n)
                .mapToObj(i -> new ScoreCard(userId, (long)i))
                .collect(Collectors.toList());
    }

}
