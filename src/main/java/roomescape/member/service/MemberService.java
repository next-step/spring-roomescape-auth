package roomescape.member.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.error.exception.MemberNotExistsException;
import roomescape.error.exception.PasswordNotMatchedException;
import roomescape.login.LoginMember;
import roomescape.member.Member;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember getLoginMemberByEmailAndPassword(String email, String password) {
        Member member = Optional.ofNullable(memberRepository.findByEmail(email))
            .orElseThrow(MemberNotExistsException::new);

        if(!member.isMatchedPassword(password)) {
            throw new PasswordNotMatchedException();
        }

        return new LoginMember(member.getId(), member.getEmail(), member.getName());
    }
}
