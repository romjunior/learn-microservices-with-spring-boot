package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;

    private MultiplicationResultAttemptRepository attemptRepository;

    private UserRepository userRepository;

    private MultiplicationRepository multiplicationRepository;

    private EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
                                     final MultiplicationResultAttemptRepository attemptRepository,
                                     final UserRepository userRepository,
                                     final MultiplicationRepository multiplicationRepository,
                                     final EventDispatcher eventDispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.multiplicationRepository = multiplicationRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt resultAttempt) {

        // Check if the user already exists for that alias
        Optional<User> user = userRepository.findOneByAlias(resultAttempt.getUser().getAlias());

        //Check if the multiplication already exists by factors
        Multiplication multiplication = multiplicationRepository.findOneByFactorAAndFactorB(resultAttempt.getMultiplication().getFactorA(), resultAttempt.getMultiplication().getFactorB());

        // Checks if it's correct
        boolean correct = resultAttempt.getResultAttempt() == resultAttempt.getMultiplication().getFactorA() * resultAttempt.getMultiplication().getFactorB();

        // Avoids 'hack' attempts
        Assert.isTrue(!resultAttempt.isCorrect(), "You can't send an attempt marked as correct!!");

        // Creates a copy, now setting the 'correct' field accordingly
        Multiplication multiplicationBase = multiplication != null && !multiplication.getId().equals(resultAttempt.getMultiplication().getId()) ? multiplication : resultAttempt.getMultiplication();

        MultiplicationResultAttempt checkedAttempt = user.map(user1 -> new MultiplicationResultAttempt(user1,
                multiplicationBase,
                resultAttempt.getResultAttempt(),
                correct)).orElseGet(() -> new MultiplicationResultAttempt(resultAttempt.getUser(),
                multiplicationBase,
                resultAttempt.getResultAttempt(),
                correct));

        // Stores the attempt
        attemptRepository.save(checkedAttempt);

        // Communicates the result via Event
        eventDispatcher.send(new MultiplicationSolvedEvent(checkedAttempt.getId(), checkedAttempt.getUser().getId(), checkedAttempt.isCorrect()));

         return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }

    @Override
    public MultiplicationResultAttempt getResultById(Long resultId) {
        return attemptRepository.findById(resultId).orElse(null);
    }

}
