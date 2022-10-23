package nextstep.theme;

import io.restassured.RestAssured;
import nextstep.E2ETest;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.member.MemberRequest;
import nextstep.support.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeE2ETest extends E2ETest {
    private static final String NONE_ADMIN_USERNAME = "NONE_ADMIN";
    private static final String NONE_ADMIN_ROLE = "NONE";
    private TokenResponse noneAdminToken;
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(NONE_ADMIN_USERNAME, PASSWORD, NAME, PHONE, NONE_ADMIN_ROLE))
                .when().post("/members")
                .then().log().all();

        noneAdminToken = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(NONE_ADMIN_USERNAME, PASSWORD))
                .when().post("/login/token")
                .then().log().all()
                .extract().as(TokenResponse.class);
    }

    @DisplayName("테마를 생성한다")
    @Test
    void create() {
        // given
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("관리자가 아닌 유저가 테마를 생성하면, 예외를 반환한다.")
    @Test
    void failToCreate() {
        // given
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(noneAdminToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .extract();
        var errorResponse = response.as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(errorResponse.getMessage()).isEqualTo("관리자만 이용가능합니다");
    }

    @DisplayName("테마 목록을 조회한다")
    @Test
    void showThemes() {
        // given
        createTheme();

        // when
        var response = RestAssured
                .given().log().all()
                .param("date", DATE)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        assertThat(response.jsonPath().getList(".")).hasSize(1);
    }

    @DisplayName("테마를 삭제한다")
    @Test
    void delete() {
        // given
        Long id = createTheme();

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("관리자가 아닌 유저가 테마를 삭제하면, 예외를 반환한다.")
    @Test
    void failToDelete() {
        // given
        Long id = createTheme();

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(noneAdminToken.getAccessToken())
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .extract();
        var errorResponse = response.as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(errorResponse.getMessage()).isEqualTo("관리자만 이용가능합니다");
    }

    public Long createTheme() {
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
        String location = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        return Long.parseLong(location.split("/")[2]);
    }
}
