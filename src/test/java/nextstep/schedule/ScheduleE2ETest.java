package nextstep.schedule;

import io.restassured.RestAssured;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScheduleE2ETest {

    private Long themeId;

    @BeforeEach
    void setUp() {
        String token = login("admin", "admin_password");
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(themeRequest)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] themeLocation = response.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);
    }

    @DisplayName("스케줄을 조회한다")
    @Test
    public void showSchedules() {
        createSchedule();

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

    public void createSchedule() {
        String token = login("admin", "admin_password");
        ScheduleRequest body = new ScheduleRequest(1L, "2022-08-11", "13:00");
        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/admin/schedules")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
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
