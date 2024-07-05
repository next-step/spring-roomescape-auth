package roomescape.repository.domain.theme.implement;

import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.theme.Themes;
import roomescape.domain.theme.vo.ThemeId;
import roomescape.repository.domain.theme.entity.ThemeEntity;
import roomescape.repository.domain.theme.mysql.MySQLJdbcThemeRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    private final MySQLJdbcThemeRepository repository;

    public ThemeRepositoryImpl(MySQLJdbcThemeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Theme save(Theme theme) {
        return repository.save(ThemeEntity.from(theme))
                .toDomain();
    }

    @Override
    public Optional<Theme> findById(ThemeId id) {
        return repository.findById(id.id())
                .map(ThemeEntity::toDomain);
    }

    @Override
    public Themes findAll() {
        return new Themes(
                repository.findAll().stream()
                        .map(ThemeEntity::toDomain)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void delete(ThemeId id) {
        repository.findById(id.id());
    }

    @Override
    public boolean existById(ThemeId id) {
        return repository.findById(id.id()).isPresent();
    }
}
