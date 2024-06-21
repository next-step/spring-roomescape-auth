package roomescape.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.auth.dto.CheckUserInfoResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.jwt.JwtTokenProvider;
import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;
import roomescape.user.exception.PasswordNotMatchException;
import roomescape.user.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("로그인에 성공한다.")
    void login() {
        // given
        User user = User.createUser(1L, "어드민", "admin@email.com", "password");

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(jwtTokenProvider.createJwt(any())).willReturn("accessToken");

        LoginRequest request = new LoginRequest(user.getEmail(), user.getPassword());

        // when
        String accessToken = authService.login(request);

        // then
        assertThat(accessToken).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("사용자 로그인 실패 - 사용자를 찾을 수 없음")
    void userLoginFailure_UserNotFound() {
        // given
        User user = User.createUser(1L, "어드민", "admin@email.com", "password");

        given(userRepository.findByEmail(anyString())).willThrow(new UserNotFoundException());

        LoginRequest request = new LoginRequest(user.getEmail(), user.getPassword());

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("사용자 로그인 실패 - 비밀번호가 일치하지 않음")
    void userLoginFailure_PasswordNotMatch() {
        // given
        User user = User.createUser(1L, "어드민", "admin@email.com", "password");

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        LoginRequest request = new LoginRequest(user.getEmail(), "12345");

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(PasswordNotMatchException.class)
                .hasMessage("비밀번호가 일치하지 않습니다");
    }

    @Test
    void 사용자_정보를_조회한다() {
        // given
        User user = User.createUser(1L, "어드민", "password", "admin@email.com");
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when
        CheckUserInfoResponse response = authService.checkUserInfo(user.getId());

        // then
        assertThat(response.name()).isEqualTo("어드민");
    }
}
