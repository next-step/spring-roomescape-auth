package roomescape.application.port.in;

import java.util.List;
import roomescape.application.dto.ThemeCommand;
import roomescape.application.dto.ThemeResponse;

public interface ThemeUseCase {

  ThemeResponse registerTheme(ThemeCommand themeCommand);

  List<ThemeResponse> retrieveThemes();

  void deleteTheme(Long id);
}
