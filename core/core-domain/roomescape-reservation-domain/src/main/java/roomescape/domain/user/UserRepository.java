package roomescape.domain.user;

import roomescape.domain.user.vo.UserEmail;
import roomescape.domain.user.vo.UserId;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(UserEmail email);

    Optional<User> findById(UserId id);

    User save(User user);
}
