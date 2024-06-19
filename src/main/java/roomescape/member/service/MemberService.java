package roomescape.member.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.error.exception.MemberNotExistsException;
import roomescape.member.Member;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean isMember(String email, String password) {
        Member member = Optional.ofNullable(memberRepository.findByEmail(email))
            .orElseThrow(MemberNotExistsException::new);

        return member.isMatchedPassword(password);
    }

    public String findNameByEmail(String email) {
        return memberRepository.findByEmail(email).getName();
    }
}
