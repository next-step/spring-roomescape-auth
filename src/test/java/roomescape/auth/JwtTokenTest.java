package roomescape.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.application.JwtTokenProvider;
import roomescape.member.domain.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JwtTokenTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("jwt 토큰 생성 후 회원 id 확인")
    void createJwtTokenAndCheckMemberId() {
        Member member = Member.of(1L, "yeeun", "GUEST", "anna862700@gmail.com", "password");

        String token = jwtTokenProvider.createToken(member);
        Long memberIdFromToken = jwtTokenProvider.extractMemberId(token);

        assertThat(memberIdFromToken).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("jwt 토큰 생성 후 회원 이름 확인")
    void createJwtTokenAndCheckMemberName() {
        Member member = Member.of(1L, "yeeun", "GUEST", "anna862700@gmail.com", "password");

        String token = jwtTokenProvider.createToken(member);
        String memberNameFromToken = jwtTokenProvider.extractMemberName(token);

        assertThat(memberNameFromToken).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("jwt 토큰 생성 후 회원 role 확인")
    void createJwtTokenAndCheckMemberRole() {
        Member member = Member.of(1L, "yeeun", "GUEST", "anna862700@gmail.com", "password");

        String token = jwtTokenProvider.createToken(member);
        String memberRoleFromToken = jwtTokenProvider.extractMemberRole(token);

        assertThat(memberRoleFromToken).isEqualTo(member.getRoleName());
    }

    @Test
    @DisplayName("토큰의 유효성 검증")
    void validateToken() {
        Member member = Member.of(1L, "yeeun", "GUEST","anna862700@gmail.com", "password");
        String token = jwtTokenProvider.createToken(member);

        boolean isValidOrNot = jwtTokenProvider.validateToken(token);

        assertThat(isValidOrNot).isTrue();
    }
}
