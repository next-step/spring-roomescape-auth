package nextstep.domain.service;

import java.util.List;
import java.util.Optional;
import nextstep.common.exception.ThemeException;
import nextstep.domain.Theme;
import nextstep.domain.repository.ThemeRepository;
import org.springframework.stereotype.Service;

@Service
public class ThemeDomainService {

    private final ThemeRepository themeRepository;

    public ThemeDomainService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Optional<Theme> findOptionalBy(String name) {
        return themeRepository.findBy(name);
    }

    public Theme findBy(String name) {
        return themeRepository.findBy(name)
            .orElseThrow(() -> new ThemeException("존재하지 않는 테마입니다."));
    }

    public Theme findBy(Long id) {
        return themeRepository.findBy(id)
            .orElseThrow(() -> new ThemeException("존재하지 않는 테마입니다,"));
    }

    public void save(Theme theme) {
        themeRepository.save(theme);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public void delete(Long id) {
        themeRepository.delete(id);
    }

    public void deleteAll() {
        themeRepository.deleteAll();
    }
}
