package roomescape.domain.theme;


import roomescape.domain.theme.vo.ThemeId;

import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);

    Optional<Theme> findById(ThemeId id);

    Themes findAll();

    void delete(ThemeId id);

    boolean existById(ThemeId id);
}
