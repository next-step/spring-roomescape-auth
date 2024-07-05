package roomescape.member.application;

import org.springframework.stereotype.Service;
import roomescape.member.domain.MemberRepository;
import roomescape.member.domain.MemberRole;
import roomescape.member.domain.entity.Member;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;

@Service
public class SignUpService {
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final PasswordEncoder passwordEncoder;

    public SignUpService(
            MemberRepository memberRepository,
            MemberValidator memberValidator,
            PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.memberValidator = memberValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponse signUp(MemberRequest memberRequest, String memberRoleName) {
        memberValidator.validateRequest(memberRequest);
        String encodedPassword = passwordEncoder.encode(memberRequest.password());
        Member member = Member.of(memberRequest, memberRoleName, encodedPassword);
        Long memberId = memberRepository.save(member);
        member.setId(memberId);
        return MemberResponse.from(member);
    }
}
