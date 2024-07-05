package roomescape.domain.theme.vo;

import roomescape.error.exception.DomainValidateException;
import roomescape.util.StringUtils;

public record ThemeThumbnail(String thumbnail) {
    public ThemeThumbnail(String thumbnail) {
        this.validateNotEmpty(thumbnail);
        this.thumbnail = thumbnail;
    }

    private void validateNotEmpty(String thumbnail) {
        if (StringUtils.isEmpty(thumbnail)) {
            throw DomainValidateException.notToBeEmpty(this.getClass());
        }
    }
}
