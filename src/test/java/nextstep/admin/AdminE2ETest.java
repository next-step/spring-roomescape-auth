package nextstep.admin;

import io.restassured.RestAssured;
import nextstep.auth.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static nextstep.auth.AuthE2ETest.PASSWORD;
import static nextstep.auth.AuthE2ETest.USERNAME;
import static nextstep.member.Member.ADMIN;
import static nextstep.member.MemberE2ETest.createMember;
import static nextstep.member.MemberE2ETest.login;
import static nextstep.schedule.ScheduleE2ETest.requestCreateSchedule;
import static nextstep.theme.ThemeE2ETest.createTheme;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdminE2ETest {

    @DisplayName("어드민 권한으로 테마를 삭제한다.")
    @Test
    public void deleteTheme() {
        Long id = createTheme();
        String token = login(ADMIN.getUsername(), ADMIN.getPassword()).as(TokenResponse.class).
                getAccessToken();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @DisplayName("어드민 권한으로 스케쥴을 삭제한다.")
    @Test
    public void deleteSchedule() {
        createTheme();
        String location = requestCreateSchedule();
        String token = login(ADMIN.getUsername(), ADMIN.getPassword()).as(TokenResponse.class).
                getAccessToken();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/admin" + location)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("어드민 권한이 아닐 시 스케쥴을 삭제할 수 없다.")
    @Test
    public void deleteScheduleFail() {
        createTheme();
        String location = requestCreateSchedule();
        createMember(USERNAME, PASSWORD, "NAME", "010-0000-0000");
        String token = login(USERNAME, PASSWORD).as(TokenResponse.class).
                getAccessToken();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/admin" + location)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}