package roomescape.member.application;

import org.springframework.stereotype.Service;
import roomescape.exception.NotFoundException;
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

    public MemberResponse findOne(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("id에 일치하는 사용자를 찾을 수 없습니다."));
        return MemberResponse.from(member);
    }
}
