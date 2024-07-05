package roomescape.application.domain.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.domain.user.api.dto.request.CreateUserRequest;
import roomescape.application.domain.user.api.dto.response.CreateUserResponse;
import roomescape.application.domain.user.service.UserService;
import roomescape.domain.user.User;

import java.net.URI;

@RestController
public class UserCommandApi {

    private final UserService userService;

    public UserCommandApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        User user = userService.create(request.toCommand());

        return ResponseEntity.created(URI.create(String.format("/users/%d", user.getId().id())))
                .body(CreateUserResponse.from(user));
    }
}
