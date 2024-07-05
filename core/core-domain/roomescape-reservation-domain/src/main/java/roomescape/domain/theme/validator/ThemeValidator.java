package roomescape.domain.theme.validator;

import roomescape.domain.theme.vo.ThemeDescription;
import roomescape.domain.theme.vo.ThemeId;
import roomescape.domain.theme.vo.ThemeName;
import roomescape.domain.theme.vo.ThemeThumbnail;
import roomescape.error.exception.DomainValidateException;

import java.util.Objects;

public final class ThemeValidator {

    private ThemeValidator() {
        throw new UnsupportedOperationException(this.getClass().getName() + "의 인스턴스는 생성되어서 안됩니다.");
    }

    public static void validate(
            ThemeId themeId,
            ThemeName name,
            ThemeDescription description,
            ThemeThumbnail thumbnail
    ) {
        validateNotNull(themeId, name, description, thumbnail);
    }

    private static void validateNotNull(
            ThemeId themeId,
            ThemeName name,
            ThemeDescription description,
            ThemeThumbnail thumbnail
    ) {
        if (Objects.isNull(themeId)) {
            throw DomainValidateException.notToBeNull(ThemeId.class);
        }
        if (Objects.isNull(name)) {
            throw DomainValidateException.notToBeNull(ThemeName.class);
        }
        if (Objects.isNull(description)) {
            throw DomainValidateException.notToBeNull(ThemeDescription.class);
        }
        if (Objects.isNull(thumbnail)) {
            throw DomainValidateException.notToBeNull(ThemeThumbnail.class);
        }
    }
}
