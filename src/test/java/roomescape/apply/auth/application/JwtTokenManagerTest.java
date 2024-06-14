package roomescape.apply.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.apply.auth.application.exception.IllegalTokenException;
import roomescape.apply.auth.ui.dto.LoginResponse;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.domain.MemberRoleNames;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JwtTokenManagerTest {

    private final JwtTokenManager jwtTokenManager;

    JwtTokenManagerTest() {
        String testKey = "TeStSeCuReKeYIsSuPeRSeCuReKeYTeStSeCuReKeYIsSuPeRSeCuReKeYTeStSeCuReKeYIsSuPeRSeCuReKeY";
        this.jwtTokenManager = new JwtTokenManager(testKey);
    }

    @Test
    @DisplayName("LoginResponse 값을 통해 토큰을 만들 수 있다.")
    void generateToken() {
        // given
        var loginResponse = new LoginResponse("tester@gmail.com",
                "test",
                MemberRoleNames.of(Set.of(MemberRoleName.GUEST)));
        // when
        String token = jwtTokenManager.generateTokenByLoginResponse(loginResponse);
        // then
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("토큰을 검증 할 수 있다.")
    void validateToken() {
        // given
        var loginResponse = new LoginResponse("tester@gmail.com",
                "test",
                MemberRoleNames.of(Set.of(MemberRoleName.GUEST)));
        String token = jwtTokenManager.generateTokenByLoginResponse(loginResponse);
        // when && then
        assertDoesNotThrow(() -> jwtTokenManager.validateToken(token));
    }

    @Test
    @DisplayName("잘못된 토큰은 검증에 실패한다")
    void validateFailToken() {
        // when && then
        assertThatThrownBy(() -> jwtTokenManager.validateToken("TOOOOOKEN")).isExactlyInstanceOf(IllegalTokenException.class);
    }


    @Test
    @DisplayName("토큰에서 저장된 사용자 권한 이름을 가져올 수 있다.")
    void getRoleNameFromToken() {
        // given
        var loginResponse = new LoginResponse("tester@gmail.com",
                "test",
                MemberRoleNames.of(Set.of(MemberRoleName.GUEST)));
        // when
        String token = jwtTokenManager.generateTokenByLoginResponse(loginResponse);
        // then
        String roleNameFromToken = jwtTokenManager.getRoleNameFromToken(token);
        assertThat(roleNameFromToken).isNotBlank().isEqualTo("게스트");
    }

}
