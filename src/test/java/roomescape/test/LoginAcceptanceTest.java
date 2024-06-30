package roomescape.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static roomescape.member.initializer.MemberInitializer.DUMMY_USER_EMAIL;
import static roomescape.member.initializer.MemberInitializer.DUMMY_USER_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.error.RoomescapeErrorMessage;
import roomescape.login.dto.LoginRequest;

@DisplayName("로그인 관련 api 호출 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/test.sql")
public class LoginAcceptanceTest {

    private final String name = "test";

    @Test
    void 로그인_성공() {
        ExtractableResponse<Response> response = 로그인(DUMMY_USER_EMAIL, DUMMY_USER_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.cookie("token")).isNotBlank();
    }

    @Test
    void 등록되지_않은_이메일을_입력할경우_로그인_실패() {
        ExtractableResponse<Response> response = 로그인("new@email.com", DUMMY_USER_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(404);
        assertThat(response.jsonPath().get("message").toString())
            .isEqualTo("해당하는 고객" + RoomescapeErrorMessage.NOT_EXISTS_EXCEPTION);
    }

    @Test
    void 일치하지_않는_비밀번호를_입력할_경우_로그인_실패() {
        ExtractableResponse<Response> response = 로그인(DUMMY_USER_EMAIL, "newPassword");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.jsonPath().get("message").toString())
            .isEqualTo(RoomescapeErrorMessage.NOT_MATCHED_PASSWORD_EXCEPTION);
    }

    @Test
    void 권한_정보가_올바르지_않으면_로그인_실패() {
        ExtractableResponse<Response> response = 로그인("illegalRole@test.com", DUMMY_USER_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.jsonPath().get("message").toString())
            .isEqualTo(RoomescapeErrorMessage.ILLEGAL_MEMBER_ROLE_EXCEPTION);
    }

    @Test
    void 로그인_후_정보_조회_성공() {
        ExtractableResponse<Response> response = 로그인(DUMMY_USER_EMAIL, DUMMY_USER_PASSWORD);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .cookie("token", response.cookie("token"))
            .when().get("/login/check")
            .then().log().all()
            .statusCode(200)
            .body("name", equalTo(name));
    }

    private ExtractableResponse<Response> 로그인(String email, String password) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new LoginRequest(email, password))
            .when().post("/login")
            .then().log().all()
            .extract();
    }
}
