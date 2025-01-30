package roomescape.auth.application.dto;

import roomescape.domain.user.domain.UserRole;

public record JwtPayload(
        UserRole role,
        String email
) {

}
