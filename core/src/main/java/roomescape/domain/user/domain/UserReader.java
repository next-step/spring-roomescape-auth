package roomescape.domain.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user for email=%s".formatted(email)));
    }

    public boolean existsByEmail(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
