package roomescape.service;

import java.util.List;

import roomescape.domain.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.ThemeRepository;
import roomescape.web.controller.dto.ThemeRequest;
import roomescape.web.controller.dto.ThemeResponse;

import org.springframework.stereotype.Service;

@Service
public class ThemeService {

	private final ThemeRepository themeRepository;

	ThemeService(ThemeRepository themeRepository) {
		this.themeRepository = themeRepository;
	}

	public List<ThemeResponse> getThemes() {
		return this.themeRepository.findAll().stream().map(ThemeResponse::from).toList();
	}

	public ThemeResponse create(ThemeRequest request) {
		var theme = Theme.builder()
			.name(request.name())
			.description(request.description())
			.thumbnail(request.thumbnail())
			.build();
		var isExistName = this.themeRepository.isExistName(request.name());
		if (isExistName) {
			throw new RoomEscapeException(ErrorCode.DUPLICATE_THEME_NAME);
		}

		var savedTheme = this.themeRepository.save(theme);
		return ThemeResponse.from(savedTheme);
	}

	public void delete(long id) {
		var isExist = this.themeRepository.isExistId(id);
		if (!isExist) {
			throw new RoomEscapeException(ErrorCode.NOT_FOUND_THEME);
		}
		this.themeRepository.delete(id);
	}

	public Theme getThemeById(long id) {
		return this.themeRepository.findById(id);
	}

}
