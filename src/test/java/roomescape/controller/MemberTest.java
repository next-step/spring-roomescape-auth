package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.MemberFixture.회원가입;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.exception.custom.DuplicateMemberException;

@Sql("classpath:table_init.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("사용자 테스트")
public class MemberTest {

    private static final int DEFAULT_ACCOUNT_SIZE = 2;
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "1234";
    private static final String NAME = "테스트";

    @Test
    @DisplayName("회원가입 테스트")
    void signup() {
        Response response = 회원가입(EMAIL, PASSWORD, NAME);

        response.then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("회원가입 시 중복된 아이디가 있는 경우 에러가 발생한다.")
    void signupDuplicateId() {
        회원가입(EMAIL, PASSWORD, NAME);

        Response response = 회원가입(EMAIL, PASSWORD, NAME);

        String message = new DuplicateMemberException().getMessage();
        response.then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("회원가입 시 올바르지 않은 값인 경우 에러가 발생한다.")
    void signupIncorrectValue() {
        Response response = 회원가입("", PASSWORD, NAME);

        String message = "이메일은 필수 값입니다.";
        response.then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("모든 회원 정보를 조회한다.(아이디, 이름)")
    void findAllMembers() {
        for (int i = 0; i < 5; i++) {
            회원가입("test" + i + "@email.com", PASSWORD, NAME);
        }

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members")
                .then().log().all()
                .body("size()", is(5 + DEFAULT_ACCOUNT_SIZE));
    }
}
