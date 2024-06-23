package roomescape.user.domain.repository;

import java.util.List;
import java.util.Optional;

import roomescape.user.domain.User;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();
}
