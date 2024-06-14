package roomescape.apply.member.application;

import org.springframework.stereotype.Service;
import roomescape.apply.auth.application.PasswordHasher;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.member.ui.dto.MemberRequest;
import roomescape.apply.member.ui.dto.MemberResponse;

import java.util.stream.Collectors;

@Service
public class MemberAdder {

    private final MemberRepository memberRepository;
    private final PasswordHasher passwordHasher;
    private final MemberDuplicateChecker duplicateChecker;
    private final MemberRoleSaver memberRoleSaver;

    public MemberAdder(MemberRepository memberRepository, PasswordHasher passwordHasher,
                       MemberDuplicateChecker duplicateChecker, MemberRoleSaver memberRoleSaver) {
        this.memberRepository = memberRepository;
        this.passwordHasher = passwordHasher;
        this.duplicateChecker = duplicateChecker;
        this.memberRoleSaver = memberRoleSaver;
    }

    public MemberResponse addNewMember(MemberRequest request) {
        duplicateChecker.checkIsDuplicateEmail(request);

        String hashPassword = passwordHasher.hashPassword(request.password());
        Member saved = memberRepository.save(Member.of(request.name(), request.email(), hashPassword));

        var memberRoles = request.roleNamesWithDefaultValue()
                .stream()
                .map(it -> MemberRole.of(it, saved.getId()))
                .collect(Collectors.toSet());
        memberRoleSaver.saveAll(memberRoles);

        return MemberResponse.from(saved);
    }

}
