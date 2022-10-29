package nextstep.schedule;

import io.restassured.RestAssured;
import nextstep.member.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static nextstep.utils.RequestFixture.*;
import static nextstep.utils.RequestFixture.tokenRequest;
import static nextstep.utils.Requests.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScheduleE2ETest {

    private Long themeId;
    private String loginToken;

    @BeforeEach
    void setUp() {
        var memberRequest = memberRequest(Role.ADMIN);
        createMemberRequest(memberRequest);
        loginToken = getLoginTokenRequest(tokenRequest(memberRequest));
        themeId = createThemeRequest(loginToken, themeRequest());
    }

    @DisplayName("스케줄을 생성한다")
    @Test
    public void createSchedule() {
        ScheduleRequest body = scheduleRequest();
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + loginToken)
                .body(body)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("관리자가 아닌 유저는 스케줄을 생성할 수 없다.")
    @Test
    public void createScheduleWithoutAdminToken() {
        var memberRequest = memberRequest(Role.USER, "userMember");
        createMemberRequest(memberRequest);
        var userLoginToken = getLoginTokenRequest(tokenRequest(memberRequest));

        ScheduleRequest body = scheduleRequest();
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + userLoginToken)
            .body(body)
            .when().post("/admin/schedules")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("스케줄을 조회한다")
    @Test
    public void showSchedules() {
        createScheduleRequest(loginToken, scheduleRequest());

        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", "2022-08-11")
                .when().get("/schedules")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
    }

    @DisplayName("스케줄을 삭제한다")
    @Test
    void delete() {
        Long scheduleId = createScheduleRequest(loginToken, scheduleRequest());

        var response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + loginToken)
                .when().delete("/admin/schedules/" + scheduleId)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
