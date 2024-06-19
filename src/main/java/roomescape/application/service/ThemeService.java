package roomescape.application.service;

import static roomescape.adapter.mapper.ThemeMapper.mapToDomain;
import static roomescape.adapter.mapper.ThemeMapper.mapToResponse;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.adapter.mapper.ThemeMapper;
import roomescape.application.dto.ThemeCommand;
import roomescape.application.dto.ThemeResponse;
import roomescape.application.port.in.ThemeUseCase;
import roomescape.application.port.out.ThemePort;
import roomescape.domain.Theme;
import roomescape.exception.NotFoundThemeException;

@Transactional
@Service
public class ThemeService implements ThemeUseCase {

  private final ThemePort themePort;

  public ThemeService(ThemePort themePort) {
    this.themePort = themePort;
  }

  @Override
  public ThemeResponse registerTheme(ThemeCommand themeCommand) {
    Theme theme = mapToDomain(themeCommand);
    return mapToResponse(themePort.saveTheme(theme));
  }

  @Override
  public List<ThemeResponse> retrieveThemes() {
    return themePort.findThemes()
                    .stream()
                    .map(ThemeMapper::mapToResponse)
                    .toList();
  }

  @Override
  public void deleteTheme(Long id) {
    if (themePort.countThemeById(id) == 0) {
      throw new NotFoundThemeException();
    }

    themePort.deleteTheme(id);
  }
}
