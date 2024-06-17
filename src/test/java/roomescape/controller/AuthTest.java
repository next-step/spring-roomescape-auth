package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.LoginRequest;

import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthTest {
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "1234";
    public static final String TOKEN_COOKIE_NAME = "token";
    public static final String NAME = "테스트";

    @DisplayName("[로그인] - 유효한 자격 증명으로 로그인하여 토큰을 획득한다.")
    @Test
    void login() {
        Response response = RestAssured
                .given().log().all()
                .body(new LoginRequest(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login");

        response.then().log().all()
                .statusCode(HttpStatus.OK.value())
                .cookie(TOKEN_COOKIE_NAME);
    }

    @DisplayName("[로그인] - 아이디 또는 비밀번호를 입력하지 않은 경우 에러가 발생한다.")
    @Test
    void loginRequiredValueException() {
        RestAssured
                .given().log().all()
                .body(new LoginRequest(EMAIL, ""))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("[로그인] - 아이디(이메일) 또는 비밀번호가 일치하지 않는 경우 에러가 발생한다.")
    @Test
    void loginMismatchValueException() {
        RestAssured
                .given().log().all()
                .body(new LoginRequest(EMAIL, "12345"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("[로그인 체크] - 유효한 토큰으로 로그인 사용자의 인증정보를 조회한다")
    @Test
    void loginCheck() {
        String token = RestAssured
                .given().log().all()
                .body(new LoginRequest(EMAIL, PASSWORD))
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
        RestAssured
                .given().log().all()
                .cookie(TOKEN_COOKIE_NAME, "")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}