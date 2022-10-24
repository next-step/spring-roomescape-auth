package nextstep.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.support.ErrorResponse;
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
    private static final String USERNAME = "newwisdom";
    private static final String PASSWORD = "123123";
    private static final String NAME = "신지혜";
    private static final String PHONE = "010-1234-5678";

    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("내 정보를 조회한다")
    @Test
    void showMe() {
        // given
        createMember(USERNAME, PASSWORD, NAME, PHONE);
        String token = login(USERNAME, PASSWORD).as(TokenResponse.class).
                getAccessToken();

        // when
        ExtractableResponse<Response> response = findMember(token);
        Member member = response.as(Member.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(member.getUsername()).isNotNull();
    }

    @DisplayName("잘못된 토큰으로 내 정보 조회 조회 시, 실패한다")
    @Test
    void failToFindMe() {
        // given, when
        ExtractableResponse<Response> response = findMember("Bearer empty");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(errorResponse.getMessage()).isEqualTo("인증에 실패하였습니다.");
    }

    private ExtractableResponse<Response> createMember(String username, String password, String name, String phone) {
        MemberRequest request = new MemberRequest(username, password, "name", "010-1234-5678");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> login(String username, String password) {
        TokenRequest request = new TokenRequest(username, password);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findMember(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
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
