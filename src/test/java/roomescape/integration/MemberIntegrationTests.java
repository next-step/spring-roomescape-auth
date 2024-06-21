package roomescape.integration;

import org.junit.jupiter.api.Test;
import roomescape.web.controller.dto.MemberRequest;
import roomescape.web.controller.dto.MemberResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
class MemberIntegrationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void create() {
		// given
		var memberRequest = new MemberRequest("nextstep", "tester@nextstep.com", "1234");

		// when
		var responseEntity = this.restTemplate.postForEntity("http://localhost:" + this.port + "/members",
				memberRequest, MemberResponse.class);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isNotNull();
		assertThat(responseEntity.getBody().id()).isNotNull();
		assertThat(responseEntity.getBody().name()).isEqualTo("nextstep");
		assertThat(responseEntity.getBody().email()).isEqualTo("tester@nextstep.com");
	}

	@Test
	void createMemberWhenExistsName() {
		// given
		var memberRequest = new MemberRequest("tester", "tester@gmail.com", "1234");

		var responseEntity = this.restTemplate.postForEntity("http://localhost:" + this.port + "/members",
				memberRequest, MemberResponse.class);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}

}
