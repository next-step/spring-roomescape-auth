package roomescape.repository.domain.user;

import roomescape.repository.domain.user.entity.UserEntity;

import java.util.Optional;

public interface UserJdbcRepository {

    UserEntity save(UserEntity entity);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);
}
