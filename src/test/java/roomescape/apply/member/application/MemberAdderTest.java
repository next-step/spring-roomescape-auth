package roomescape.apply.member.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.member.application.mock.MockPasswordHasher;
import roomescape.apply.member.domain.repository.MemberJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.member.domain.repository.MemberRoleJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRoleRepository;
import roomescape.apply.member.ui.dto.MemberResponse;
import roomescape.support.BaseTestService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.memberRequest;

class MemberAdderTest extends BaseTestService {

    private MemberAdder memberAdder;
    private MemberRepository memberRepository;
    private MemberRoleRepository memberRoleRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        memberRepository = new MemberJDBCRepository(template);
        memberRoleRepository = new MemberRoleJDBCRepository(template);

        var passwordHasher = new MockPasswordHasher();
        var memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
        var memberRoleSaver = new MemberRoleSaver(memberRoleRepository);
        var memberDuplicateChecker = new MemberDuplicateChecker(new MemberFinder(passwordHasher, memberRepository, memberRoleFinder));
        memberAdder = new MemberAdder(memberRepository,
                passwordHasher,
                memberDuplicateChecker,
                memberRoleSaver
        );
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("새로운 맴버를 추가한다.")
    void saveMemberTest() {
        // given
        var request = memberRequest();
        // when
        MemberResponse memberResponse = memberAdder.addNewMember(request);
        // then
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.name()).isEqualTo(request.name());
        var member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow();
        List<String> savedRoleNames = memberRoleRepository.findNamesByMemberId(member.getId());
        assertThat(savedRoleNames).isNotNull().hasSize(2)
                .containsExactlyInAnyOrder("GUEST", "ADMIN");
    }

}