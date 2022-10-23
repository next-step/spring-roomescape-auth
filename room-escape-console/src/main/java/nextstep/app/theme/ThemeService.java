package nextstep.app.theme;

import nextstep.domain.theme.Theme;
import nextstep.domain.theme.usecase.ThemeRepository;

import java.util.List;

public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public void save(String name, String desc, Long price) {
        themeRepository.save(new Theme(null, name, desc, price));
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public void delete(Long reservationId) {
        themeRepository.delete(reservationId);
    }
}
