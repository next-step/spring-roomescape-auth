package roomescape.domain.user.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserEntityRepository implements UserRepository {

    private final UserJdbcRepository userJdbcRepository;

    @Override
    public User save(final User user) {
        return userJdbcRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userJdbcRepository.findByEmail(email);
    }
}
