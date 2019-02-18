package microservices.book.gamification.event;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.service.GameService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventHandler {

    private GameService gameService;

    @Autowired
    EventHandler(final GameService gameService) {
        this.gameService = gameService;
    }


    @RabbitListener(queues = "${multiplication.queue}")
    void handleMultiplicationSolved(final MultiplicationSolvedEvent event) {
        log.info("Multiplication Solved Event received: {}", event.getMultiplicationResultAttempt());

        try {
            gameService.newAttemptForUser(event.getUserId(), event.getMultiplicationResultAttempt(), event.isCorrect());
        }catch (final Exception e) {
            log.error("Error when trying to process MultiplicationSolvedEvent", e);
            // Avoids the event to be re-queued and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
