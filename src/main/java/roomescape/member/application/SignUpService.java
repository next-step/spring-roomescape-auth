package roomescape.member.application;

import org.springframework.stereotype.Service;
import roomescape.member.domain.MemberRepository;
import roomescape.member.ui.dto.MemberRequest;

@Service
public class SignUpService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(MemberRequest memberRequest) {
        memberRepository.save(
                memberRequest.name(),
                memberRequest.email(),
                passwordEncoder.encode(memberRequest.password())
        );
    }
}
