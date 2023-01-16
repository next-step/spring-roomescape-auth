package nextstep.app.web.member.adapter.in;

import nextstep.core.member.Member;
import nextstep.core.member.in.MemberLoginRequest;
import nextstep.core.member.in.MemberRegisterRequest;
import nextstep.core.member.in.MemberResponse;
import nextstep.core.member.in.MemberUseCase;
import nextstep.core.member.out.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
    public MemberResponse login(MemberLoginRequest request) {
        Member member = repository.findByUsername(request.getUsername())
                .filter(it -> it.checkWrongPassword(request.getPassword()))
                .orElseThrow(() -> new NoSuchElementException("요청한 정보와 일치하는 회원이 존재하지 않습니다."));
        return MemberResponse.from(member);
    }

    @Override
    public MemberResponse findMember(Long memberId) {
        Member member = repository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("요청한 정보와 일치하는 회원이 존재하지 않습니다."));
        return MemberResponse.from(member);
    }
}
