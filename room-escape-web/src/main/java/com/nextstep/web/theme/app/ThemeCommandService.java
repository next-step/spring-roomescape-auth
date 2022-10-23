package com.nextstep.web.theme.app;

import com.nextstep.web.theme.dto.CreateThemeRequest;
import nextstep.domain.theme.Theme;
import nextstep.domain.theme.usecase.ThemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ThemeCommandService {
    private final ThemeRepository themeRepository;

    public ThemeCommandService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Long save(CreateThemeRequest request) {
        return themeRepository.save(new Theme(null, request.getName(), request.getDesc(), request.getPrice()));
    }

    public void delete(Long id) {
        themeRepository.delete(id);
    }
}
