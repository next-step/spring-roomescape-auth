package roomescape.domain.user.vo;

import roomescape.util.StringUtils;

public record UserPassword(String password) {

    public boolean isEmpty() {
        return StringUtils.isEmpty(password);
    }
}
