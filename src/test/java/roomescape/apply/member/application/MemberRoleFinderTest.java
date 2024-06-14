package roomescape.apply.member.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.repository.MemberJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.member.domain.repository.MemberRoleJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRoleRepository;
import roomescape.support.BaseTestService;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.*;

class MemberRoleFinderTest extends BaseTestService {

    private MemberRoleFinder memberRoleFinder;
    private MemberRepository memberRepository;
    private MemberRoleRepository memberRoleRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        memberRepository = new MemberJDBCRepository(template);
        memberRoleRepository = new MemberRoleJDBCRepository(template);

        memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @ParameterizedTest
    @MethodSource("provideMemberRoles")
    @DisplayName("맴버가 가진 모든 권한을 찾을 수 있다.")
    void findRolesInMember(String email, Set<String> expectedRoles) {
        // given
        Member member = memberRepository.save(member(memberRequest(email)));
        memberRoleRepository.saveAll(expectedRoles.stream().map(it -> memberRole(it, member.getId())).iterator());
        // when
        Set<MemberRoleName> rolesInMember = memberRoleFinder.findRolesInMember(member.getId());
        // then
        assertThat(rolesInMember).isNotEmpty();
        Set<String> resultRoleName = rolesInMember.stream().map(MemberRoleName::getValue).collect(Collectors.toSet());
        assertThat(expectedRoles).isEqualTo(resultRoleName);
    }

    private static Stream<Arguments> provideMemberRoles() {
        return Stream.of(
                Arguments.of("admin@gmail.com", Set.of(MemberRoleName.ADMIN.getValue())),
                Arguments.of("guest@gmail.com", Set.of(MemberRoleName.GUEST.getValue())),
                Arguments.of("both@gmail.com", Set.of(MemberRoleName.ADMIN.getValue(), MemberRoleName.GUEST.getValue()))
        );
    }

}