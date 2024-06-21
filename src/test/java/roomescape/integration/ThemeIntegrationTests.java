package roomescape.integration;

import java.util.List;

import org.junit.jupiter.api.Test;
import roomescape.web.controller.dto.ThemeRequest;
import roomescape.web.controller.dto.ThemeResponse;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class ThemeIntegrationTests {

	private final TestRestTemplate restTemplate = new TestRestTemplate();

	@LocalServerPort
	private int port;

	@Test
	void themeControllerEndpoints() {
		// create theme
		// given
		ThemeRequest themeRequest = new ThemeRequest("테마1", "첫번째테마", "썸네일이미지");

		// when
		var createResponse = this.restTemplate.postForEntity("http://localhost:" + this.port + "/themes", themeRequest,
				ThemeResponse.class);

		// then
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		ThemeResponse themeResponse = createResponse.getBody();
		assertThat(themeResponse).isNotNull();
		assertThat(themeResponse.name()).isEqualTo("테마1");
		assertThat(themeResponse.description()).isEqualTo("첫번째테마");
		assertThat(themeResponse.thumbnail()).isEqualTo("썸네일이미지");

		// get themes
		// when
		var themesResponse = this.restTemplate.getForEntity("http://localhost:" + this.port + "/themes", List.class);

		// then
		assertThat(themesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		var themes = themesResponse.getBody();
		assertThat(themes).isNotNull();
		assertThat(themes.size()).isEqualTo(1);

		// delete theme
		// given
		long themeId = themeResponse.id();

		// when
		var deleteResponse = this.restTemplate.exchange("http://localhost:" + this.port + "/themes/" + themeId,
				HttpMethod.DELETE, null, Void.class);

		// then
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// check theme delete
		// when
		themesResponse = this.restTemplate.getForEntity("http://localhost:" + this.port + "/themes", List.class);

		// then
		assertThat(themesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		themes = themesResponse.getBody();
		assertThat(themes).isNotNull();
		assertThat(themes.size()).isEqualTo(0);
	}

}
