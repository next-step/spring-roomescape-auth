package roomescape.view;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViewTest {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String TOKEN = "token";
    private static final String ROLE = "role";
    private static String token = null;

    @BeforeEach
    void 회원가입을_하고_로그인한_뒤_관리자_권한을_받는다() {

        /* 회원가입 */
        //given
        Map<String, Object> joinMember = new HashMap<>();
        joinMember.put(EMAIL, "test@naver.com");
        joinMember.put(PASSWORD, "password123");
        joinMember.put(NAME, "박민욱");

        //when
        ExtractableResponse<Response> joinResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(joinMember)
                .when().post("/members")
                .then().log().all()
                .extract();

        //then
        assertThat(joinResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(joinResponse.jsonPath().getString(NAME)).isEqualTo("박민욱");

        /* 로그인 */
        Map<String, Object> loginMember = new HashMap<>();
        loginMember.put(EMAIL, "test@naver.com");
        loginMember.put(PASSWORD, "password123");

        //when
        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginMember)
                .when().post("/login")
                .then().log().all()
                .extract();

        //then
        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(loginResponse.cookie(TOKEN)).isNotNull();
        token = loginResponse.cookie(TOKEN);

        /* 관리자 권한 받기 */
        //when
        ExtractableResponse<Response> updateAdminRoleResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .when().post("/members/role")
                .then().log().all()
                .extract();

        //then
        assertThat(updateAdminRoleResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateAdminRoleResponse.jsonPath().getString(ROLE)).isEqualTo("ADMIN");
    }

    @Test
    void 사용자향_예약_관리_페이지를_랜더링한다() {

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    void 관리자향_예약_관리_페이지를_랜더링한다() {

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie(TOKEN, token)
                .when().get("/admin/reservation")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 관리자향_시간_관리_페이지를_랜더링한다() {

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie(TOKEN, token)
                .when().get("/admin/time")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 관리자향_테마_관리_페이지를_랜더링한다() {

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie(TOKEN, token)
                .when().get("/admin/theme")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 로그인_페이지를_랜더링한다() {

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
