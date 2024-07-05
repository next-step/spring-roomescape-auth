package roomescape.domain.reservation.vo;

import roomescape.error.exception.DomainValidateException;
import roomescape.util.StringUtils;

public record ReservationName(String name) {

    public ReservationName(String name) {
        this.validateNotEmpty(name);
        this.name = name;
    }

    private void validateNotEmpty(String name) {
        if (StringUtils.isEmpty(name)) {
            throw DomainValidateException.notToBeEmpty(this.getClass());
        }
    }
}
