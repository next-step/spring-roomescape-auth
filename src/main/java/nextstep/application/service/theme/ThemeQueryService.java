package nextstep.application.service.theme;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.application.dto.theme.ThemeResponse;
import nextstep.domain.Theme;
import nextstep.domain.service.ThemeDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ThemeQueryService {

    private final ThemeDomainService themeDomainService;

    public ThemeQueryService(ThemeDomainService themeDomainService) {
        this.themeDomainService = themeDomainService;
    }

    public List<ThemeResponse> checkAll() {
        return themeDomainService.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public ThemeResponse checkBy(Long id) {
        return toResponse(themeDomainService.findBy(id));
    }

    private ThemeResponse toResponse(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDesc(), theme.getPrice());
    }
}
