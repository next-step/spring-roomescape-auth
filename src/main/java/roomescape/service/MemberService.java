package roomescape.service;

import roomescape.controller.dto.LoginRequest;
import roomescape.controller.dto.MemberRequest;
import roomescape.controller.dto.MemberResponse;
import roomescape.domain.Member;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.MemberRepository;

import org.springframework.stereotype.Service;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public MemberResponse create(MemberRequest request) {
		var member = Member.builder().name(request.name()).email(request.email()).password(request.password()).build();

		var isExists = this.memberRepository.isExists(request.name());
		if (isExists) {
			throw new RoomEscapeException(ErrorCode.DUPLICATE_MEMBER);
		}
		var savedMember = this.memberRepository.save(member);
		return MemberResponse.from(savedMember);
	}

	public MemberResponse findMemberByLoginRequest(LoginRequest request) {
		var findedMember = this.memberRepository.findByEmail(request.email());

		if (findedMember == null) {
			throw new RoomEscapeException(ErrorCode.NOT_FOUND_MEMBER);
		}
		checkPassword(request.password(), findedMember.getPassword());
		return MemberResponse.from(findedMember);
	}

	private void checkPassword(String inputPassword, String storedPassword) {
		if (!inputPassword.equals(storedPassword)) {
			throw new RoomEscapeException(ErrorCode.INVALID_PASSWORD);
		}
	}

}
