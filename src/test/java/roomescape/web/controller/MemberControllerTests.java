package roomescape.web.controller;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import roomescape.web.controller.dto.MemberRequest;
import roomescape.web.controller.dto.MemberResponse;
import roomescape.domain.MemberRole;
import roomescape.service.MemberService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class MemberControllerTests {

	@InjectMocks
	private MemberController memberController;

	@Mock
	private MemberService memberService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	void create() {
		// given
		MemberRequest memberRequest = new MemberRequest("이름", "tester@gmail.com", "1234");
		MemberResponse memberResponse = new MemberResponse(1L, "이름", "tester@gmail.com", MemberRole.USER.name());

		given(this.memberService.create(any(MemberRequest.class))).willReturn(memberResponse);

		// when
		ResponseEntity<MemberResponse> responseEntity = this.memberController.create(memberRequest);

		// then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getBody()).isEqualTo(memberResponse);
		SoftAssertions.assertSoftly((softly) -> {
			softly.assertThat(responseEntity.getBody()).isNotNull();
			softly.assertThat(responseEntity.getBody().id()).isEqualTo(memberResponse.id());
			softly.assertThat(responseEntity.getBody().name()).isEqualTo(memberResponse.name());
			softly.assertThat(responseEntity.getBody().email()).isEqualTo(memberResponse.email());
			softly.assertThat(responseEntity.getBody().role()).isEqualTo(memberResponse.role());
		});
	}

}
