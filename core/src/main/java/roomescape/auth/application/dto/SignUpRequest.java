package roomescape.auth.application.dto;

import roomescape.domain.user.domain.UserRole;

public record SignUpRequest(
        String email,
        String password,
        String name,
        UserRole role
) {

}
