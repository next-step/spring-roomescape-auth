package roomescape.apply.member.application;

import org.springframework.stereotype.Service;
import roomescape.apply.auth.application.PasswordHasher;
import roomescape.apply.auth.ui.dto.LoginRequest;
import roomescape.apply.auth.ui.dto.LoginResponse;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.MemberRoleNames;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.member.ui.dto.MemberResponse;

import java.util.List;
import java.util.Set;

@Service
public class MemberFinder {

    private static final String LOGIN_FAIL_MESSAGE = "아이디 혹은 비밀번호가 잘못되었습니다.";

    private final PasswordHasher passwordHasher;
    private final MemberRepository memberRepository;
    private final MemberRoleFinder memberRoleFinder;

    public MemberFinder(PasswordHasher passwordHasher, MemberRepository memberRepository, MemberRoleFinder memberRoleFinder) {
        this.passwordHasher = passwordHasher;
        this.memberRepository = memberRepository;
        this.memberRoleFinder = memberRoleFinder;
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    public LoginResponse findByLoginRequest(LoginRequest request) {
        String hashPassword = passwordHasher.hashPassword(request.password());

        Member member = memberRepository.findByEmailAndPassword(request.email(), hashPassword)
                .orElseThrow(() -> new IllegalArgumentException(LOGIN_FAIL_MESSAGE));

        Set<MemberRoleName> rolesInMember = memberRoleFinder.findRolesInMember(member.getId());

        return LoginResponse.from(member, MemberRoleNames.of(rolesInMember));
    }

    public boolean isDuplicateEmail(String email) {
        return memberRepository.countByEmail(email) != 0;
    }
}
