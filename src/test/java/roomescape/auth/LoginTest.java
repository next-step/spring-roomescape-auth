package roomescape.auth;

import io.restassured.RestAssured;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.application.AuthService;
import roomescape.auth.ui.dto.LoginCheckResponse;
import roomescape.auth.ui.dto.LoginRequest;
import roomescape.member.application.SignUpService;
import roomescape.member.ui.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginTest {
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private AuthService loginService;
    private final String NAME = "yeeun";
    private final String EMAIL = "anna862700@gmail.com";
    private final String PASSWORD = "password";

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    @Test
    @DisplayName("로그인")
    void login() {
        signUpService.signUp(new MemberRequest(NAME, EMAIL, PASSWORD), "GUEST");

        var response = RestAssured
                .given().log().all()
                .body(new LoginRequest(EMAIL, PASSWORD))
                .contentType("application/json")
                .when().post("/login")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.cookie("token")).isNotEmpty();
        assertThat(response.cookie("token").split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("로그인 성공 후 얻은 토큰 통해서 회원 이름 얻기")
    void getMemberNameThroughTokenAfterLoginSuccess() {
        signUpService.signUp(new MemberRequest(NAME, EMAIL, PASSWORD), "GUEST");
        Cookie token = loginService.login(new LoginRequest(EMAIL, PASSWORD));

        var response = RestAssured
                .given().log().all()
                .cookie(token.getName(), token.getValue())
                .when().get("/login/check")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(LoginCheckResponse.class).name()).isEqualTo(NAME);
    }
}
