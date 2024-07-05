package roomescape.domain.theme.vo;

import roomescape.error.exception.DomainValidateException;
import roomescape.util.StringUtils;

public record ThemeName(String name) {

    public ThemeName(String name) {
        this.validateNotEmpty(name);
        this.name = name;
    }

    private void validateNotEmpty(String name) {
        if (StringUtils.isEmpty(name)) {
            throw DomainValidateException.notToBeEmpty(this.getClass());
        }
    }
}
