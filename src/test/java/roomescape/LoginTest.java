package roomescape;

import io.restassured.RestAssured;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.application.LoginService;
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
    private LoginService loginService;
    private final String NAME = "yeeun";
    private final String EMAIL = "anna862700@gmail.com";
    private final String PASSWORD = "password";

    public LoginTest() {
        RestAssured.port = 8888;
    }

    @Test
    @DisplayName("LoginController - login()")
    void 로그인() {
        signUpService.signUp(new MemberRequest(NAME, EMAIL, PASSWORD));

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
    @DisplayName("LoginController - readMemberName()")
    void 로그인_후_받은_토큰_통해_사용자_이름_얻기() {
        signUpService.signUp(new MemberRequest(NAME, EMAIL, PASSWORD));
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
