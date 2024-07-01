package roomescape.theme.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.ui.dto.ThemeRequest;

@Service
public class ThemeValidator {
    private final ThemeRepository themeRepository;

    public ThemeValidator(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public void checkPresent(Long themeId) {
        themeRepository.findById(themeId)
                .orElseThrow(() -> NotFoundException.of("존재하지 않는 테마입니다."));
    }

    public void validateRequest(ThemeRequest themeRequest) {
        checkDuplicated(themeRequest.name());
    }

    private void checkDuplicated(String name) {
        boolean isDuplicated = themeRepository.findByName(name).isPresent();

        if (isDuplicated) {
            throw BadRequestException.of("이미 존재하는 테마입니다.");
        }
    }
}
