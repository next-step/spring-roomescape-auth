package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.AuthFixture.로그인;
import static roomescape.fixture.MemberFixture.회원가입;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.LoginRequest;
import roomescape.exception.custom.PasswordMismatchException;
import roomescape.exception.custom.TokenNotFoundException;

@DisplayName("인증 및 인가 관련 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthTest {
    private static final String MEMBER_EMAIL = "test@email.com";
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String PASSWORD = "1234";
    public static final String TOKEN_COOKIE_NAME = "token";
    public static final String NAME = "테스트";

    @BeforeEach
    void init() {
        회원가입(MEMBER_EMAIL, PASSWORD, NAME);
    }

    @DisplayName("[로그인] - 유효한 자격 증명으로 로그인하여 토큰을 획득한다.")
    @Test
    void login() {
        Response response = 로그인(MEMBER_EMAIL, PASSWORD);

        response.then().log().all()
                .statusCode(HttpStatus.OK.value())
                .cookie(TOKEN_COOKIE_NAME);
    }


    @DisplayName("[로그인] - 아이디 또는 비밀번호를 입력하지 않은 경우 에러가 발생한다.")
    @Test
    void loginRequiredValueException() {
        String message = "비밀번호는 필수 값입니다.";

        RestAssured
                .given().log().all()
                .body(new LoginRequest(MEMBER_EMAIL, ""))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @DisplayName("[로그인] - 아이디(이메일) 또는 비밀번호가 일치하지 않는 경우 에러가 발생한다.")
    @Test
    void loginMismatchValueException() {
        String message = new PasswordMismatchException().getMessage();

        RestAssured
                .given().log().all()
                .body(new LoginRequest(MEMBER_EMAIL, "12345"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @DisplayName("[로그인 체크] - 유효한 토큰으로 로그인 사용자의 인증정보를 조회한다")
    @Test
    void loginCheck() {
        String token = RestAssured
                .given().log().all()
                .body(new LoginRequest(MEMBER_EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie(TOKEN_COOKIE_NAME);

        Response response = RestAssured
                .given().log().all()
                .cookie(TOKEN_COOKIE_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check");

        response.then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("name", is(NAME));
    }

    @DisplayName("[로그인 체크] - 올바르지 않은 쿠키인 경우 로그인 체크시 에러가 발생한다.")
    @Test
    void loginCheckIncorrectTokenException() {
        String message = new TokenNotFoundException().getMessage();

        RestAssured
                .given().log().all()
                .cookie(TOKEN_COOKIE_NAME, "")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @DisplayName("[권한] - 관리자 권한이 있는 경우 관리자 페이지에 접근에 성공한다.")
    @Test
    void adminPageAccessSuccess() {
        Response response = 로그인(ADMIN_EMAIL, PASSWORD);
        String token = response.then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie(TOKEN_COOKIE_NAME);

        RestAssured
                .given().log().all()
                .cookie(TOKEN_COOKIE_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("[권한] - 관리자 페이지 접근 시 권한이 없는 경우 에러가 발생한다.")
    @Test
    void adminPageAccessUnauthorized() {
        Response response = 로그인(MEMBER_EMAIL, PASSWORD);
        String token = response.then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie(TOKEN_COOKIE_NAME);

        RestAssured
                .given().log().all()
                .cookie(TOKEN_COOKIE_NAME, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
