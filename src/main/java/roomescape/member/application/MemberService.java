package roomescape.member.application;

import org.springframework.stereotype.Service;
import roomescape.member.domain.MemberRepository;
import roomescape.member.domain.entity.Member;
import roomescape.member.ui.dto.MemberResponse;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();
        return MemberResponse.fromMembers(members);
    }
}
