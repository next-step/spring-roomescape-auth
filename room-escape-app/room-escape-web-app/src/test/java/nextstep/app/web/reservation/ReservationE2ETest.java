package nextstep.app.web.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.app.web.auth.AuthE2ETest;
import nextstep.app.web.member.MemberRegisterWebRequest;
import nextstep.core.member.MemberRole;
import nextstep.core.reservation.Reservation;
import nextstep.core.schedule.in.ScheduleCreateRequest;
import nextstep.core.theme.in.ThemeCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private final AuthE2ETest authE2ETest = new AuthE2ETest();
    private ReservationCreateWebRequest request;
    private Long themeId;
    private Long scheduleId;
    private Long memberId;

    @BeforeEach
    void setUp() {
        ThemeCreateRequest themeRequest = new ThemeCreateRequest("테마이름", "테마설명", 22000L);
        var themeResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

        ScheduleCreateRequest scheduleRequest = new ScheduleCreateRequest(themeId, LocalDate.parse(DATE), LocalTime.parse(TIME));
        var scheduleResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest)
                .when().post("/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        MemberRegisterWebRequest body = new MemberRegisterWebRequest("username", "password", "name", MemberRole.USER, "010-1234-5678");
        var memberResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        String[] memberLocation = memberResponse.header("Location").split("/");
        memberId = Long.parseLong(memberLocation[memberLocation.length - 1]);

        request = new ReservationCreateWebRequest(scheduleId);
    }

    @DisplayName("예약을 생성한다")
    @Test
    void create() {
        String token = authE2ETest.create();

        var response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", "Bearer " + token)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약을 조회한다")
    @Test
    void show() {
        String token = authE2ETest.create();
        createReservation(token);

        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<ReservationWebResponse> reservations = response.jsonPath().getList(".", ReservationWebResponse.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void delete() {
        String token = authE2ETest.create();
        var reservation = createReservation(token);

        var response = RestAssured
                .given().log().all()
                .header("authorization", "Bearer " + token)
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("중복 예약을 생성한다")
    @Test
    void createDuplicateReservation() {
        String token = authE2ETest.create();
        createReservation(token);

        String anotherToken = authE2ETest.create();
        var response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", "Bearer " + anotherToken)
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
        String token = authE2ETest.create();
        var response = RestAssured
                .given().log().all()
                .header("authorization", "Bearer " + token)
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> createReservation(String token) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", "Bearer " + token)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }
}
