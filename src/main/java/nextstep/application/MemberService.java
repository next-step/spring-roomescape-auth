package nextstep.application;

import nextstep.domain.Member;
import nextstep.domain.repository.MemberRepository;
import nextstep.presentation.dto.member.MemberRequest;
import nextstep.presentation.dto.member.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long create(MemberRequest request) {
        Member member = new Member(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getPhone()
        );

        return memberRepository.save(member);
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id);

        return new MemberResponse(
            member.getId(),
            member.getUsername(),
            member.getPassword(),
            member.getName(),
            member.getPhone(),
            ""
        );
    }
}
