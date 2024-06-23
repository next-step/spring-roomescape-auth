package roomescape.user.application;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;
import roomescape.user.dto.UserResponse;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(u -> new UserResponse(u.getId(), u.getName()))
                .toList();
    }
}
