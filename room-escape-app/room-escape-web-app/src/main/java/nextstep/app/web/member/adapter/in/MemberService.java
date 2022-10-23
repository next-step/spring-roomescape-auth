package nextstep.app.web.member.adapter.in;

import nextstep.core.member.Member;
import nextstep.core.member.in.MemberRegisterRequest;
import nextstep.core.member.in.MemberResponse;
import nextstep.core.member.in.MemberUseCase;
import nextstep.core.member.out.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService implements MemberUseCase {
    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MemberResponse register(MemberRegisterRequest request) {
        Member member = repository.save(request.to());
        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse login(Long id) {
        Member member = repository.findById(id);
        return MemberResponse.from(member);
    }
}
