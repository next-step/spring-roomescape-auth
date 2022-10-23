package nextstep.admin;

import static nextstep.member.MemberRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.member.MemberRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdminE2ETest {

    private static final String DATE = "2022-08-11";
    private static final String TIME = "13:00";

    private Long themeId;
    private Long scheduleId;

    @DisplayName("어드민이 테마를 생성한다.")
    @Test
    void adminCreateTheme() {
        String token = login("admin", "admin_password");
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);

        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("어드민이 아닌 사용자가 테마를 생성할 수 없다.")
    @Test
    public void nonAdminCreateTheme() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);

        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("어드민이 테마를 삭제한다.")
    @Test
    void adminDeleteTheme() {
        String token = login("admin", "admin_password");
        Long themeId = createTheme(token);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().delete("/admin/themes/" + themeId)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("어드민이 아닌 사용자가 테마를 삭제할 수 없다.")
    @Test
    void nonAdminDeleteTheme() {
        String adminToken = login("admin", "admin_password");
        Long themeId = createTheme(adminToken);

        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().delete("/admin/themes/" + themeId)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("어드민이 스케줄을 생성한다.")
    @Test
    void adminCreateSchedule() {
        createTheme();
        String token = login("admin", "admin_password");
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);

        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(scheduleRequest)
            .when().post("/admin/schedules")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    @DisplayName("어드민이 아닌 사용자가 스케줄을 생성할 수 없다.")
    @Test
    void nonAdminCreateSchedule() {
        createTheme();
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);

        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(scheduleRequest)
            .when().post("/admin/schedules")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .extract();
    }

    @DisplayName("어드민이 스케줄을 삭제한다")
    @Test
    void adminDeleteSchedule() {
        createSchedule();
        String token = login("admin", "admin_password");

        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/admin/schedules/" + scheduleId)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

    @DisplayName("어드민이 아닌 사용자는 스케줄을 삭제할 수 없다.")
    @Test
    void nonAdminDeleteSchedule() {
        createSchedule();
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");

        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/admin/schedules/" + scheduleId)
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .extract();
    }

    private void createSchedule() {
        createTheme();
        String token = login("admin", "admin_password");
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);

        var scheduleResponse = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(scheduleRequest)
            .when().post("/admin/schedules")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();

        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
    }

    private void createTheme() {
        String token = login("admin", "admin_password");
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var themeResponse = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(themeRequest)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);
    }

    private void createMember(String username, String password) {
        MemberRequest body = new MemberRequest(
            username,
            password,
            "name",
            "010-1234-5678",
            USER
        );
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    private Long createTheme(String token) {
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);

        String location = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().header("Location");

        return Long.parseLong(location.split("/")[2]);
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
}
