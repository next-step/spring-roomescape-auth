package roomescape.domain.user.vo;

import roomescape.util.StringUtils;

public record UserName(String name) {

    public boolean isEmpty() {
        return StringUtils.isEmpty(name);
    }
}
