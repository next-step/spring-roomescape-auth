package roomescape.auth.api.response;

import roomescape.domain.user.domain.UserRole;

public record LoginCheckResponse(
        String userName,
        UserRole userRole
) {
}
