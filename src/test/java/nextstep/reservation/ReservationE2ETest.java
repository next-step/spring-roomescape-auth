package nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";
    public static final String NAME = "name";

    private ReservationRequest request;
    private Long themeId;
    private Long scheduleId;
    private Long memberId;

    @BeforeEach
    void setUp() {
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
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

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
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

        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678");
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

        request = new ReservationRequest(
                scheduleId,
                "브라운"
        );
    }

    @DisplayName("예약을 생성한다")
    @Test
    void create() {
        var response = RestAssured
                .given().log().all()
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
        createReservation();

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
        var reservation = createReservation();

        var response = RestAssured
                .given().log().all()
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("중복 예약을 생성한다")
    @Test
    void createDuplicateReservation() {
        createReservation();

        var response = RestAssured
                .given().log().all()
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
        var response = RestAssured
                .given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> createReservation() {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }
}
