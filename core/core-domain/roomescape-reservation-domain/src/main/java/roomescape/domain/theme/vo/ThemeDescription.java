package roomescape.domain.theme.vo;

import roomescape.error.exception.DomainValidateException;
import roomescape.util.StringUtils;

public record ThemeDescription(String description) {

    public ThemeDescription(String description) {
        this.validateNotEmpty(description);
        this.description = description;
    }

    private void validateNotEmpty(String description) {
        if (StringUtils.isEmpty(description)) {
            throw DomainValidateException.notToBeEmpty(this.getClass());
        }
    }
}
