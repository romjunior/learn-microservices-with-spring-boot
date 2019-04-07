package microservices.book.multiplication.controller;

import microservices.book.multiplication.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public final class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUserById(@PathVariable("userId") final Long userId) {
        return userRepository.findById(userId).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }
}
