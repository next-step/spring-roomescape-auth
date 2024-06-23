package roomescape.member.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.error.exception.MemberNotExistsException;
import roomescape.error.exception.PasswordNotMatchedException;
import roomescape.login.LoginMember;
import roomescape.member.Member;
import roomescape.member.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findMembers() {
        return memberRepository.find().stream()
            .map(member -> new MemberResponse(member.getId(), member.getEmail(), member.getName()))
            .collect(Collectors.toList());
    }

    public LoginMember getLoginMemberByEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotExistsException::new);

        if (!member.isMatchedPassword(password)) {
            throw new PasswordNotMatchedException();
        }

        return new LoginMember(member.getId(), member.getEmail(), member.getName());
    }
}
