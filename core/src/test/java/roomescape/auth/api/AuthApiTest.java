package roomescape.auth.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import roomescape.auth.application.dto.LoginRequest;
import roomescape.auth.application.dto.SignUpRequest;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserRepository;
import roomescape.domain.user.domain.UserRole;
import roomescape.fixture.LoginFixture;
import roomescape.support.RestAssuredTestSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthApiTest extends RestAssuredTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginFixture loginFixture;

    @DisplayName("이메일, 비밀번호, 이름, 역할을 이용하여 회원가입 가능")
    @Test
    void postSignUp() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest(
                "test@email.com",
                "password",
                "name",
                UserRole.CUSTOMER
        );

        // when
        ValidatableResponse response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signUpRequest)
                .when().post("/api/signup")
                .then().log().all();

        // then
        response.statusCode(200);

        User user = userRepository.findByEmail("test@email.com").get();
        assertAll(
                () -> assertThat(user.getEmail()).isEqualTo("test@email.com"),
                () -> assertThat(user.getPassword()).isEqualTo("password"),
                () -> assertThat(user.getName()).isEqualTo("name"),
                () -> assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER)
        );
    }

    @DisplayName("중복된 이메일로 회원가입 시 400 반환")
    @Test
    void postSignUp_email_already_in_use() {
        // given
        User user = User.builder()
                .email("test@email.com")
                .password("pw")
                .name("test-name")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(user);

        SignUpRequest signUpRequest = new SignUpRequest(
                "test@email.com",
                "password",
                "name",
                UserRole.CUSTOMER
        );

        // when
        ValidatableResponse response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signUpRequest)
                .when().post("/api/signup")
                .then().log().all();

        // then
        response.statusCode(400);
    }

    @DisplayName("로그인에 성공 시 로그인 토큰(accessToken)을 발급한다")
    @Test
    void issue_login_token() {
        // given
        final User user = User.builder()
                .role(UserRole.CUSTOMER)
                .email("email")
                .password("password")
                .name("name")
                .build();
        userRepository.save(user);

        final LoginRequest loginRequest = new LoginRequest("email", "password");

        // when
        final ValidatableResponse actual = RestAssured
                .given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/login")
                .then().log().all();

        // then
        final Response response = actual
                .statusCode(200)
                .extract()
                .response();

        final String accessToken = response.getCookie("accessToken");
        assertThat(accessToken).isNotBlank();
    }

    // todo: 로그인 실패 시 401 반환하는 API 테스트 추가

    @DisplayName("로그아웃 시 accessToken 쿠키를 삭제한다")
    @Test
    void logout() {
        // given
        String accessToken = loginFixture.loginWithUserName("홍길동");

        // when
        final ValidatableResponse response = RestAssured
                .given().log().all()
                .cookie("accessToken", accessToken)
                .when().post("/api/logout")
                .then().log().all();

        // then
        String accessTokenCookie = response.extract().cookie("accessToken");
        assertThat(accessTokenCookie).isEmpty();
    }

    @DisplayName("로그인한 유저의 인증 정보 조회 시 유저 이름과 권한 반환")
    @Test
    void login_check() {
        // given
        final User user = User.builder()
                .role(UserRole.CUSTOMER)
                .email("email")
                .password("password")
                .name("user-name")
                .build();
        userRepository.save(user);

        final String accessToken = loginFixture.login(user);

        // when
        final ValidatableResponse actual = RestAssured
                .given().log().all()
                .cookie("accessToken", accessToken) // fixme: 인증이 필요한 모든 API에 대해 accessToken을 추가해야하는 번거로움을 어떻게 해결할지?
                .when().get("/api/login/check")
                .then().log().all();

        // then
        actual
                .statusCode(200)
                .body("data.userName", equalTo("user-name"))
                .body("data.userRole", equalTo(UserRole.CUSTOMER.name()))
                .extract()
                .response();
    }
}
