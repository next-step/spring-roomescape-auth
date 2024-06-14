package roomescape.apply.member.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.member.application.mock.MockPasswordHasher;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.repository.MemberJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.member.domain.repository.MemberRoleJDBCRepository;
import roomescape.support.BaseTestService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;

class MemberDuplicateCheckerTest extends BaseTestService {

    private MemberDuplicateChecker memberDuplicateChecker;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        memberRepository = new MemberJDBCRepository(template);
        var memberRoleRepository = new MemberRoleJDBCRepository(template);

        var passwordHasher = new MockPasswordHasher();
        var memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
        var memberFinder = new MemberFinder(passwordHasher, memberRepository, memberRoleFinder);
        memberDuplicateChecker = new MemberDuplicateChecker(memberFinder);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("이메일이 중복되었는지 체크한다.")
    void checkIsDuplicateEmail() {
        // given
        var request = memberRequest();
        Member member = member(request);
        // when
        memberRepository.save(member);
        // then
        assertThatThrownBy(() -> memberDuplicateChecker.checkIsDuplicateEmail(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일 입니다.");
    }


}