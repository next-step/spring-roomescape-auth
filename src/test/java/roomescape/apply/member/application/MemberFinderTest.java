package roomescape.apply.member.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.auth.ui.dto.LoginRequest;
import roomescape.apply.auth.ui.dto.LoginResponse;
import roomescape.apply.member.application.mock.MockPasswordHasher;
import roomescape.apply.member.domain.repository.MemberJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.member.domain.repository.MemberRoleJDBCRepository;
import roomescape.apply.member.ui.dto.MemberResponse;
import roomescape.support.BaseTestService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.MemberFixture.memberRequest;

class MemberFinderTest extends BaseTestService {


    private MemberFinder memberFinder;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        memberRepository = new MemberJDBCRepository(template);
        var memberRoleRepository = new MemberRoleJDBCRepository(template);

        var passwordHasher = new MockPasswordHasher();
        var memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
        memberFinder = new MemberFinder(passwordHasher, memberRepository, memberRoleFinder);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("모든 맴버를 찾는다.")
    void saveMemberTest() {
        // given
        memberRepository.save(member(memberRequest("newbie1", "newbie1@gmail.com")));
        memberRepository.save(member(memberRequest("newbie2", "newbie2@gmail.com")));
        memberRepository.save(member(memberRequest("newbie3", "newbie3@gmail.com")));
        // when
        List<MemberResponse> allMembers = memberFinder.findAll();
        // then
        assertThat(allMembers).isNotNull().hasSize(4);
    }

    @Test
    @DisplayName("로그인 정보로 맴버를 찾는다.")
    void findByLoginRequest() {
        // given
        String targetName = "newbie2";
        memberRepository.save(member(memberRequest("newbie1", "newbie1@gmail.com")));
        memberRepository.save(member(memberRequest(targetName, "newbie2@gmail.com")));
        memberRepository.save(member(memberRequest("newbie3", "newbie3@gmail.com")));
        LoginRequest request = new LoginRequest("newbie2@gmail.com", "123", "");
        // when
        LoginResponse loginResponse = memberFinder.findByLoginRequest(request);
        // then
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.name()).isEqualTo(targetName);
        assertThat(loginResponse.email()).isEqualTo(request.email());
        assertThat(loginResponse.memberRoleNames()).isNotNull();
    }


    @Test
    @DisplayName("중복된 이메일이 있는지 확인한다.")
    void isDuplicateEmail() {
        // given
        String targetEmail = "newbie1@gmail.com";
        memberRepository.save(member(memberRequest("newbie1", targetEmail)));
        // when
        boolean shouldTrue = memberFinder.isDuplicateEmail(targetEmail);
        boolean shouldFalse = memberFinder.isDuplicateEmail("NOT_EXIST@gmail.com");
        // then
        assertThat(shouldTrue).isTrue();
        assertThat(shouldFalse).isFalse();
    }
}