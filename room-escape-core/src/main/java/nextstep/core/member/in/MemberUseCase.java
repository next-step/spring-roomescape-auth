package nextstep.core.member.in;

public interface MemberUseCase {
    MemberResponse register(MemberRegisterRequest request);

    MemberResponse login(Long id);
}
