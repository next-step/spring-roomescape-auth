package roomescape.theme.domain;

import roomescape.theme.domain.entity.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    List<Theme> findAll();
    Optional<Theme> findById(Long id);
    Optional<Theme> findByName(String name);
    Long countReservationMatchWith(Long id);
    long save(Theme theme);
    long deleteById(Long id);
}
