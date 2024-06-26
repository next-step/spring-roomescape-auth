package roomescape.service;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.web.controller.dto.ThemeRequest;
import roomescape.domain.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.ThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class ThemeServiceTests {

	@InjectMocks
	private ThemeService themeService;

	@Mock
	private ThemeRepository themeRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getThemes() {
		// given
		List<Theme> themes = new ArrayList<>();

		Theme theme = Theme.builder().id(1L).name("테마1").description("첫번째테마").thumbnail("썸네일이미지").build();

		themes.add(theme);

		given(this.themeRepository.findAll()).willReturn(themes);

		// when
		var resultThemes = this.themeService.getThemes();

		// then
		assertThat(resultThemes).isNotEmpty();
		assertThat(resultThemes).hasSize(1);
		assertThat(resultThemes).allSatisfy((themeResponse) -> {
			assertThat(themeResponse.id()).isEqualTo(1L);
			assertThat(themeResponse.name()).isEqualTo("테마1");
			assertThat(themeResponse.description()).isEqualTo("첫번째테마");
			assertThat(themeResponse.thumbnail()).isEqualTo("썸네일이미지");
		});
	}

	@Test
	void create() {
		// given
		Theme theme = Theme.builder().name("테마1").description("첫번째테마").thumbnail("썸네일이미지").build();
		ThemeRequest request = new ThemeRequest("테마1", "첫번째테마", "썸네일이미지");

		given(this.themeRepository.isExistName(request.name())).willReturn(false);
		given(this.themeRepository.save(theme)).willAnswer((invocationOnMock) -> {
			Theme savedTheme = invocationOnMock.getArgument(0);
			savedTheme.setId(1L);
			return savedTheme;
		});

		// when
		var createdTheme = this.themeService.create(request);

		// then
		SoftAssertions.assertSoftly((softly) -> {
			softly.assertThat(createdTheme).isNotNull();
			softly.assertThat(createdTheme.id()).isEqualTo(1L);
			softly.assertThat(createdTheme.name()).isEqualTo("테마1");
			softly.assertThat(createdTheme.description()).isEqualTo("첫번째테마");
			softly.assertThat(createdTheme.thumbnail()).isEqualTo("썸네일이미지");
		});
	}

	@Test
	void deleteWhenThemeNotFound() {
		// given
		long id = 1L;

		// when, then
		assertThatThrownBy(() -> this.themeService.delete(id)).isInstanceOf(RoomEscapeException.class)
			.hasMessage(ErrorCode.NOT_FOUND_THEME.getMessage());
	}

	@Test
	void getThemeById() {
		// given
		Theme theme = Theme.builder().id(1L).name("테마1").description("첫번째테마").thumbnail("썸네일이미지").build();

		given(this.themeRepository.findById(anyLong())).willReturn(theme);

		// when
		var resultThemeById = this.themeService.getThemeById(1L);

		// then
		SoftAssertions.assertSoftly((softly) -> {
			softly.assertThat(resultThemeById).isNotNull();
			softly.assertThat(resultThemeById.getId()).isEqualTo(1L);
			softly.assertThat(resultThemeById.getName()).isEqualTo("테마1");
			softly.assertThat(resultThemeById.getDescription()).isEqualTo("첫번째테마");
			softly.assertThat(resultThemeById.getThumbnail()).isEqualTo("썸네일이미지");
		});

	}

	@Test
	void createExceptionWhenDuplicateThemeName() {
		// given
		ThemeRequest request = new ThemeRequest("테마1", "첫번째테마", "썸네일이미지");

		given(this.themeRepository.isExistName(request.name())).willReturn(true);

		// when, then
		assertThatThrownBy(() -> this.themeService.create(request)).isInstanceOf(RoomEscapeException.class)
			.hasMessage(ErrorCode.DUPLICATE_THEME_NAME.getMessage());
	}

}
