package roomescape.repository.domain.theme;

import roomescape.repository.domain.theme.entity.ThemeEntity;

import java.util.List;
import java.util.Optional;

public interface ThemeJdbcRepository {

    ThemeEntity save(ThemeEntity entity);

    Optional<ThemeEntity> findById(Long id);

    List<ThemeEntity> findAll();

    void delete(Long id);
}
