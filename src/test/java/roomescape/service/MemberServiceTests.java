package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.MemberRepository;
import roomescape.web.controller.dto.LoginRequest;
import roomescape.web.controller.dto.MemberRequest;
import roomescape.web.controller.dto.MemberResponse;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class MemberServiceTests {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void create() {
		// given
		MemberRequest memberRequest = new MemberRequest("이름", "tester@gmail.com", "1234");
		Member member = Member.builder()
			.id(1L)
			.name("이름")
			.email("tester@gmail.com")
			.password("encodedPassword")
			.role(MemberRole.USER.name())
			.build();

		MemberResponse memberResponse = new MemberResponse(1L, "이름", "tester@gmail.com", MemberRole.USER.name());

		given(this.memberRepository.isExists(memberRequest.name())).willReturn(false);
		given(this.memberRepository.save(any(Member.class))).willReturn(member);

		// when
		var createdMember = this.memberService.create(memberRequest);

		// then
		assertThat(createdMember).isEqualTo(memberResponse);
		assertThat(createdMember.id()).isEqualTo(1L);
		assertThat(createdMember.name()).isEqualTo("이름");
		assertThat(createdMember.email()).isEqualTo("tester@gmail.com");
		assertThat(createdMember.role()).isEqualTo(MemberRole.USER.name());
	}

	@Test
	void createWhenMemberAlreadyExists() {
		// given
		MemberRequest memberRequest = new MemberRequest("이름", "tester@gmail.com", "1234");

		given(this.memberRepository.isExists(memberRequest.name())).willReturn(true);

		// when, then
		assertThatThrownBy(() -> this.memberService.create(memberRequest)).isInstanceOf(RoomEscapeException.class)
			.hasMessageContaining(ErrorCode.DUPLICATE_MEMBER.getMessage());
	}

	@Test
	void findMemberByLoginRequest() {
		// given
		LoginRequest loginRequest = new LoginRequest("tester@gmail.com", "1234");
		Member member = Member.builder()
			.id(1L)
			.name("이름")
			.email("tester@gmail.com")
			.password(this.passwordEncoder.encode("1234"))
			.role(MemberRole.USER.name())
			.build();
		MemberResponse memberResponse = new MemberResponse(1L, "이름", "tester@gmail.com", MemberRole.USER.name());

		given(this.passwordEncoder.matches(loginRequest.password(), member.getPassword())).willReturn(true);
		given(this.memberRepository.findByEmail(loginRequest.email())).willReturn(member);

		// when
		MemberResponse actualResponse = this.memberService.findMemberByLoginRequest(loginRequest);

		// then
		assertThat(actualResponse).isEqualTo(memberResponse);
	}

	@Test
	void findMemberByLoginRequestWhenMemberDoesNotExist() {
		// given
		LoginRequest loginRequest = new LoginRequest("tester@gmail.com", this.passwordEncoder.encode("1234"));

		given(this.memberRepository.findByEmail(loginRequest.email())).willReturn(null);

		// when, then
		assertThatThrownBy(() -> this.memberService.findMemberByLoginRequest(loginRequest))
			.isInstanceOf(RoomEscapeException.class)
			.hasMessageContaining(ErrorCode.NOT_FOUND_MEMBER.getMessage());
	}

	@Test
	void findMemberByLoginRequestWhenPasswordDoesNotMatch() {
		// given
		LoginRequest loginRequest = new LoginRequest("tester@gmail.com", this.passwordEncoder.encode("4444"));
		Member member = Member.builder()
			.id(1L)
			.name("이름")
			.email("tester@gmail.com")
			.password(this.passwordEncoder.encode("1234"))
			.role(MemberRole.USER.name())
			.build();

		given(this.memberRepository.findByEmail(loginRequest.email())).willReturn(member);

		// when, then
		assertThatThrownBy(() -> this.memberService.findMemberByLoginRequest(loginRequest))
			.isInstanceOf(RoomEscapeException.class)
			.hasMessageContaining(ErrorCode.INVALID_PASSWORD.getMessage());
	}

}
