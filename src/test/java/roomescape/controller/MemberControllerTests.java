package roomescape.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.controller.dto.MemberRequest;
import roomescape.controller.dto.MemberResponse;
import roomescape.domain.MemberRole;
import roomescape.service.MemberService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
	}

}
