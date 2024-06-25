package roomescape.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.web.controller.ThemeController;
import roomescape.web.controller.dto.ThemeRequest;
import roomescape.web.controller.dto.ThemeResponse;
import roomescape.service.ThemeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class ThemeControllerTests {

	@InjectMocks
	private ThemeController themeController;

	@Mock
	private ThemeService themeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	void getThemes() {
		// given
		List<ThemeResponse> themeResponses = List.of(new ThemeResponse(1L, "테마1", "첫번째테마", "이미지"));

		given(this.themeService.getThemes()).willReturn(themeResponses);

		// when
		ResponseEntity<List<ThemeResponse>> responseEntity = this.themeController.getThemes();

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).isEqualTo(themeResponses);
		SoftAssertions.assertSoftly((softly) -> {
			softly.assertThat(responseEntity.getBody()).isNotNull();
			softly.assertThat(responseEntity.getBody().get(0).id()).isEqualTo(themeResponses.get(0).id());
			softly.assertThat(responseEntity.getBody().get(0).name()).isEqualTo(themeResponses.get(0).name());
			softly.assertThat(responseEntity.getBody().get(0).description()).isEqualTo(themeResponses.get(0).description());
			softly.assertThat(responseEntity.getBody().get(0).thumbnail()).isEqualTo(themeResponses.get(0).thumbnail());
		});
	}

	@Test
	void create() {
		// given
		var themeRequest = new ThemeRequest("테마1", "첫번째테마", "테마이미지");
		var themeResponse = new ThemeResponse(1L, "테마1", "첫번째테마", "테마이미지");

		given(this.themeService.create(themeRequest)).willReturn(themeResponse);

		// when
		ResponseEntity<ThemeResponse> responseEntity = this.themeController.create(themeRequest);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isEqualTo(themeResponse);
		SoftAssertions.assertSoftly((softly) -> {
			softly.assertThat(responseEntity.getBody()).isNotNull();
			softly.assertThat(responseEntity.getBody().id()).isEqualTo(themeResponse.id());
			softly.assertThat(responseEntity.getBody().name()).isEqualTo(themeResponse.name());
			softly.assertThat(responseEntity.getBody().description()).isEqualTo(themeResponse.description());
			softly.assertThat(responseEntity.getBody().thumbnail()).isEqualTo(themeResponse.thumbnail());
		});
	}

	@Test
	void delete() {
		// given
		long id = 1L;

		// when
		ResponseEntity<Void> responseEntity = this.themeController.delete(id);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(this.themeService).delete(id);
	}

}
