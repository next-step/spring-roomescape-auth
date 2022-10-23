package nextstep.domain.theme.usecase;

import nextstep.domain.theme.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    Long save(Theme theme);
    Optional<Theme> findById(Long id);
    List<Theme> findAll();
    void delete(Long id);
}
