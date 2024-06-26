package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;

import roomescape.domain.Member;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.MemberRepository;
import roomescape.web.controller.dto.LoginRequest;
import roomescape.web.controller.dto.MemberRequest;
import roomescape.web.controller.dto.MemberResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;

	MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public MemberResponse create(MemberRequest request) {
		var encodedPassword = this.passwordEncoder.encode(request.password());
		var member = Member.builder().name(request.name()).email(request.email()).password(encodedPassword).build();

		var isExists = this.memberRepository.isExists(request.name());
		if (isExists) {
			throw new RoomEscapeException(ErrorCode.DUPLICATE_MEMBER);
		}
		var savedMember = this.memberRepository.save(member);
		return MemberResponse.from(savedMember);
	}

	public MemberResponse findMemberByLoginRequest(LoginRequest request) {
		var foundMember = this.memberRepository.findByEmail(request.email());

		if (foundMember == null) {
			throw new RoomEscapeException(ErrorCode.NOT_FOUND_MEMBER);
		}
		checkPassword(request.password(), foundMember.getPassword());
		return MemberResponse.from(foundMember);
	}

	public List<MemberResponse> findAllMembersViaRoleUser() {
		return this.memberRepository.findAllMembersViaRoleUser()
			.stream()
			.map((member) -> new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole()))
			.collect(Collectors.toList());
	}

	public Member findById(Long id) {
		var foundMember = this.memberRepository.findById(id);

		if (foundMember == null) {
			throw new RoomEscapeException(ErrorCode.NOT_FOUND_MEMBER);
		}
		return foundMember;
	}

	private void checkPassword(String inputPassword, String storedPassword) {
		if (!this.passwordEncoder.matches(inputPassword, storedPassword)) {
			throw new RoomEscapeException(ErrorCode.INVALID_PASSWORD);
		}
	}

}
