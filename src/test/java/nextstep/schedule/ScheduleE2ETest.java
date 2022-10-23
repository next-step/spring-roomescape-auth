package nextstep.schedule;

import io.restassured.RestAssured;
import nextstep.E2ETest;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.member.MemberRequest;
import nextstep.support.ErrorResponse;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class ScheduleE2ETest extends E2ETest {
    private static final String NONE_ADMIN_USERNAME = "NONE_ADMIN";
    private static final String NONE_ADMIN_ROLE = "NONE";
    private TokenResponse noneAdminToken;
    private Long themeId;

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

        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .extract();

        String[] themeLocation = response.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);
    }

    @DisplayName("스케줄을 생성한다")
    @Test
    void createSchedule() {
        // given
        ScheduleRequest body = new ScheduleRequest(themeId, DATE, TIME);

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/schedules")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("관리자가 아닌 유저가 스케줄을 생성하면, 예외를 반환한다.")
    @Test
    void failToCreate() {
        // given
        ScheduleRequest body = new ScheduleRequest(themeId, DATE, TIME);

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(noneAdminToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/schedules")
                .then().log().all()
                .extract();
        var errorResponse = response.as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(errorResponse.getMessage()).isEqualTo("관리자만 이용가능합니다");
    }

    @DisplayName("스케줄을 조회한다")
    @Test
    void showSchedules() {
        // given
        requestCreateSchedule();

        // when
        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/schedules")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        assertThat(response.jsonPath().getList(".")).hasSize(1);
    }

    @DisplayName("스케줄을 삭제한다")
    @Test
    void delete() {
        // given
        String location = requestCreateSchedule();

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/admin" + location)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("관리자가 아닌 유저가 스케줄을 삭제하면, 예외를 반환한다.")
    @Test
    void failToDelete() {
        // given
        String location = requestCreateSchedule();

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(noneAdminToken.getAccessToken())
                .when().delete("/admin" + location)
                .then().log().all()
                .extract();
        var errorResponse = response.as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(errorResponse.getMessage()).isEqualTo("관리자만 이용가능합니다");
    }

    public String requestCreateSchedule() {
        ScheduleRequest body = new ScheduleRequest(1L, DATE, TIME);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/schedules")
                .then().log().all()
                .extract().header("Location");
    }
}
