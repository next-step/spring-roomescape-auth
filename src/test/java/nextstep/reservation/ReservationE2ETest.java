package nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.member.MemberRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static nextstep.member.MemberRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationE2ETest {

    private static final String DATE = "2022-08-11";
    private static final String TIME = "13:00";

    private ReservationRequest request;
    private Long themeId;

    @BeforeEach
    void setUp() {
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
        Long scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        MemberRequest body = new MemberRequest(
            "username",
            "password",
            "name",
            "010-1234-5678",
            USER
        );
        var memberResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();

        String[] memberLocation = memberResponse.header("Location").split("/");
        Long memberId = Long.parseLong(memberLocation[memberLocation.length - 1]);

        request = new ReservationRequest(
            scheduleId,
            "최현구"
        );
    }

    @DisplayName("예약을 생성한다")
    @Test
    void create() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약을 조회한다")
    @Test
    void show() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");
        createReservationWithToken(token);

        var response = RestAssured
            .given().log().all()
            .param("themeId", themeId)
            .param("date", DATE)
            .when().get("/reservations")
            .then().log().all()
            .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void delete() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");
        var reservation = createReservationWithToken(token);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().delete(reservation.header("Location"))
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("로그인한 사용자의 예약이 아니면 삭제할 수 없다.")
    @Test
    void deleteException() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");
        var reservation = createReservationWithToken(token);

        createMember("피의전사 브라운", "죽음의 데스");
        String anotherToken = login("피의전사 브라운", "죽음의 데스");

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(anotherToken)
            .when().delete(reservation.header("Location"))
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("중복 예약을 생성한다")
    @Test
    void createDuplicateReservation() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");
        createReservationWithToken(token);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약이 없을 때 예약 목록을 조회한다")
    @Test
    void showEmptyReservations() {
        var response = RestAssured
            .given().log().all()
            .param("themeId", themeId)
            .param("date", DATE)
            .when().get("/reservations")
            .then().log().all()
            .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(0);
    }

    @DisplayName("없는 예약을 삭제한다")
    @Test
    void createNotExistReservation() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().delete("/reservations/1")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> createReservationWithToken(String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();
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
