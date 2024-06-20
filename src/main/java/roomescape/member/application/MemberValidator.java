package roomescape.member.application;

import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.member.domain.MemberRepository;
import roomescape.member.ui.dto.MemberRequest;

@Service
public class MemberValidator {
    private final MemberRepository memberRepository;

    public MemberValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void validateRequest(MemberRequest memberRequest) {
        checkDuplicated(memberRequest.email());
    }

    private void checkDuplicated(String email) {
        boolean isDuplicated = memberRepository.findByEmail(email).isPresent();

        if (isDuplicated) {
            throw BadRequestException.of("중복된 이메일입니다.");
        }
    }
}
