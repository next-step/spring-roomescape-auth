package roomescape.application.port.out;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemePort {

  Theme saveTheme(Theme theme);

  List<Theme> findThemes();

  void deleteTheme(Long id);

  Integer countThemeById(Long id);

    Optional<Theme> findThemeById(Long id);
}
