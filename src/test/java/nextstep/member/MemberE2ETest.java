package nextstep.member;

import io.restassured.RestAssured;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberE2ETest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        MemberRequest body = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678");
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("access token으로 내 정보를 조회한다.")
    @Test
    void me() {
        MemberRequest createBody = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678");
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(createBody)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenRequestBody = new TokenRequest(USERNAME, PASSWORD);
        var accessToken = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequestBody)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(TokenResponse.class);

        var memberResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("authorization", accessToken.getAccessToken())
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        Member member = memberResponse.as(Member.class);
        assertThat(member).isNotNull();
        assertThat(member.getUsername()).isEqualTo(USERNAME);
        assertThat(member.getPassword()).isEqualTo(PASSWORD);
    }

    //
//    @DisplayName("테마 목록을 조회한다")
//    @Test
//    public void showThemes() {
//        createTheme();
//
//        var response = RestAssured
//                .given().log().all()
//                .param("date", "2022-08-11")
//                .when().get("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value())
//                .extract();
//        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
//    }
//
//    @DisplayName("테마를 삭제한다")
//    @Test
//    void delete() {
//        Long id = createTheme();
//
//        var response = RestAssured
//                .given().log().all()
//                .when().delete("/themes/" + id)
//                .then().log().all()
//                .extract();
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
//
//    public Long createTheme() {
//        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
//        String location = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(body)
//                .when().post("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.CREATED.value())
//                .extract().header("Location");
//        return Long.parseLong(location.split("/")[2]);
//    }
}
