package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, multiplicationResultAttemptRepository, userRepository, multiplicationRepository, eventDispatcher);
    }

    @Test
    public void createRandomMultiplicationTest() {
        // given (our mocked Random Generator service will return first 50, then 30)
        BDDMockito.given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        //when
        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        //assert
        Assertions.assertThat(multiplication.getFactorA()).isEqualTo(50);
        Assertions.assertThat(multiplication.getFactorB()).isEqualTo(30);
        Assertions.assertThat(multiplication.getResult()).isEqualTo(1500);
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), true);
        BDDMockito.given(userRepository.findOneByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //assert
        Assertions.assertThat(attemptResult).isTrue();
        BDDMockito.verify(multiplicationResultAttemptRepository).save(verifiedAttempt);
        BDDMockito.verify(multiplicationRepository).findOneByFactorAAndFactorB(attempt.getMultiplication().getFactorA(), attempt.getMultiplication().getFactorB());
        BDDMockito.verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempt.getId(), attempt.getUser().getId(), false);
        BDDMockito.given(userRepository.findOneByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //assert
        Assertions.assertThat(attemptResult).isFalse();
        BDDMockito.verify(multiplicationResultAttemptRepository).save(attempt);
        BDDMockito.verify(multiplicationRepository).findOneByFactorAAndFactorB(attempt.getMultiplication().getFactorA(), attempt.getMultiplication().getFactorB());
        BDDMockito.verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void retrieveStatsTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt1 = new
                MultiplicationResultAttempt(
                user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new
                MultiplicationResultAttempt(
                user, multiplication, 3051, false);
        List<MultiplicationResultAttempt> latestAttempts =
                Lists.newArrayList(attempt1, attempt2);
        BDDMockito.given(userRepository.findOneByAlias("john_doe")).willReturn(Optional.empty());
        BDDMockito.given(multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe"))
                .willReturn(latestAttempts);

        // when
        List<MultiplicationResultAttempt> latestAttemptsResult =
                multiplicationServiceImpl.
                        getStatsForUser("john_doe");

        // then
        Assertions.assertThat(latestAttemptsResult).isEqualTo
                (latestAttempts);
    }

    @Test
    public void getResultByIdTest() {
        //given
        Long resultId = 4L;
        User user = new User("jhon_doe");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt expectedAttempt = new MultiplicationResultAttempt(user, multiplication, 3500, true);
        BDDMockito.given(multiplicationResultAttemptRepository.findById(resultId)).willReturn(Optional.of(expectedAttempt));

        //when
        MultiplicationResultAttempt attempt = multiplicationServiceImpl.getResultById(resultId);

        //then
        Assertions.assertThat(attempt).isEqualTo(expectedAttempt);

    }
}
