package roomescape.user.domain.repository;

import java.util.Optional;

import roomescape.user.domain.User;

public interface UserRepository {

    Optional<User> findByEmail(String email);
}
