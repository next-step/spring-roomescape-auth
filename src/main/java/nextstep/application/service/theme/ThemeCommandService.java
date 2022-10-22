package nextstep.application.service.theme;

import java.util.Optional;
import nextstep.application.dto.theme.ThemeRequest;
import nextstep.common.exception.ThemeException;
import nextstep.domain.Theme;
import nextstep.domain.service.ThemeDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ThemeCommandService {

    private final ThemeDomainService themeDomainService;

    public ThemeCommandService(ThemeDomainService themeDomainService) {
        this.themeDomainService = themeDomainService;
    }

    public Long create(ThemeRequest request) {
        Optional<Theme> theme = themeDomainService.findOptionalBy(request.getName());

        if (theme.isPresent()) {
            throw new ThemeException(String.format("%s는(은) 이미 존재하는 테마입니다.", request.getName()));
        }
        themeDomainService.save(toTheme(request));

        return themeDomainService.findBy(request.getName()).getId();
    }

    private Theme toTheme(ThemeRequest request) {
        return new Theme(request.getName(), request.getDesc(), request.getPrice());
    }

    public void delete(Long id) {
        themeDomainService.delete(id);
    }

    public void deleteAll() {
        themeDomainService.deleteAll();
    }
}
