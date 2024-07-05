package roomescape.domain.user.vo;

import roomescape.util.StringUtils;

public record UserEmail(String email) {

    public boolean isEmpty() {
        return StringUtils.isEmpty(email);
    }
}
