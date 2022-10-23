package nextstep.member;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ThemeE2ETest {

    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678", "developer");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    private void createMember(String username, String password) {
        MemberRequest body = new MemberRequest(username, password, "name", "010-1234-5678", "developer");
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    private String login(String username, String password) {
        TokenRequest body = new TokenRequest(username, password);
        TokenResponse response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/login")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().as(TokenResponse.class);

        return response.accessToken;
    }

    @DisplayName("로그인 후 내 정보를 조회한다.")
    @Test
    void me() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");

        MemberResponse response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().as(MemberResponse.class);

        assertThat(response.getUsername()).isEqualTo("hyeon9mak");
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
