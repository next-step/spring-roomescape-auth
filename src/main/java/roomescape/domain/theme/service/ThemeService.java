package roomescape.domain.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.api.dto.ThemeRequest;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.domain.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(ThemeRequest themeRequest) {
        Theme theme = new Theme(null, themeRequest.getName(), themeRequest.getDescription(), themeRequest.getThumbnail());
        Long id = themeRepository.save(theme);
        return findById(id);
    }

    @Transactional(readOnly = true)
    public Theme findById(Long id) {
        return themeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public void delete(Long id) {
        themeRepository.delete(id);
    }
}
