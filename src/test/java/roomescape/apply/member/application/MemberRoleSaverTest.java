package roomescape.apply.member.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.domain.repository.MemberJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.member.domain.repository.MemberRoleJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRoleRepository;
import roomescape.support.BaseTestService;

import java.util.List;
import java.util.Set;

import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;

class MemberRoleSaverTest extends BaseTestService {
    private MemberRoleSaver memberRoleSaver;
    private MemberRepository memberRepository;
    private MemberRoleRepository memberRoleRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        memberRepository = new MemberJDBCRepository(template);
        memberRoleRepository = new MemberRoleJDBCRepository(template);

        memberRoleSaver = new MemberRoleSaver(memberRoleRepository);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("맴버는 다수의 권한을 가질 수 있다.")
    void memberRoleSaverTest() {
        // given
        Member member = memberRepository.save(member(memberRequest()));
        Long memberId = member.getId();
        MemberRole guestRole = MemberRole.of("게스트", memberId);
        MemberRole adminRole = MemberRole.of("어드민", memberId);
        // when
        memberRoleSaver.saveAll(Set.of(guestRole, adminRole));
        // then
        List<String> roleNamesInMembers = memberRoleRepository.findNamesByMemberId(memberId);
        Assertions.assertThat(roleNamesInMembers).isNotEmpty().hasSize(2)
                .containsExactlyInAnyOrder("GUEST", "ADMIN");

    }


}