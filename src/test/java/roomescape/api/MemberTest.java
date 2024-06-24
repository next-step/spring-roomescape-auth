package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberTest {

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String TOKEN = "token";
    private static final String ROLE = "role";
    private static String token = null;

    @Test
    void 회원가입을_한다() {

        //given
        Map<String, Object> member = new HashMap<>();
        member.put(EMAIL, "test@naver.com");
        member.put(PASSWORD, "password123");
        member.put(NAME, "박민욱");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post("/members")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString(NAME)).isEqualTo("박민욱");
    }


    @Test
    void 아이디와_비밀번호로_로그인을_하고_쿠키를_전달_받는다() {

        회원가입을_한다();
        Map<String, Object> member = new HashMap<>();
        member.put(EMAIL, "test@naver.com");
        member.put(PASSWORD, "password123");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post("/login")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.cookie(TOKEN)).isNotNull();
        token = response.cookie(TOKEN);
    }

    @Test
    void 쿠키의_토큰을_이용해서_사용자_정보를_조회_및_전달_할_수_있다() {

        //given
        아이디와_비밀번호로_로그인을_하고_쿠키를_전달_받는다();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .when().get("/login/check")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString(NAME)).isEqualTo("박민욱");
    }

    @Test
    void 관리자_권한을_받을_수_있다() {

        //given
        아이디와_비밀번호로_로그인을_하고_쿠키를_전달_받는다();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .when().post("/members/role")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString(ROLE)).isEqualTo("ADMIN");
    }
}
