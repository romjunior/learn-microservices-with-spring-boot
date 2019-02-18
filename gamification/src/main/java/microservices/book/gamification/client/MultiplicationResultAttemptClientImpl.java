package microservices.book.gamification.client;

import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This implementation of MultiplicationResultAttemptClient
 interface connects to
 * the Multiplication microservice via REST.
 */
@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {

    private final RestTemplate restTemplate;
    private final String multiplicationHost;

    @Autowired
    public MultiplicationResultAttemptClientImpl(final RestTemplate restTemplate, @Value("${multiplication.host}") final String multiplicationHost) {
        this.restTemplate = restTemplate;
        this.multiplicationHost = multiplicationHost;
    }

    @Override
    public MultiplicationResultAttempt retrieveMultiplicationResultAttemptbyId(Long multiplicationId) {
        return restTemplate.getForObject(multiplicationHost + "/results/" + multiplicationId, MultiplicationResultAttempt.class);
    }
}
