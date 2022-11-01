package nextstep.reservation;

import io.restassured.RestAssured;
import nextstep.member.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static nextstep.utils.RequestFixture.*;
import static nextstep.utils.Requests.*;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationE2ETest {

    public static final String DATE = "2022-08-11";

    private ReservationRequest request;
    private Long themeId;
    private String loginToken;

    @BeforeEach
    void setUp() {
        var memberRequest = memberRequest(Role.ADMIN);
        createMemberRequest(memberRequest);
        loginToken = getLoginTokenRequest(tokenRequest(memberRequest));

        themeId = createThemeRequest(loginToken, themeRequest());
        var scheduleId = createScheduleRequest(loginToken, scheduleRequest(themeId, DATE));
        request = reservationRequest(scheduleId);
    }

    @DisplayName("예약을 생성한다")
    @Test
    void create() {
        var response = RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + loginToken)
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("로그인 없이는 예약을 생성할 수 없다")
    @Test
    void createWithoutLogin() {
        var response = RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약을 조회한다")
    @Test
    void show() {

        createReservationRequest(loginToken, request);

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
        var reservation = createReservationRequest(loginToken, request);

        var response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + loginToken)
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("자신의 예약이 아닌 경우 예약 취소가 불가능하다.")
    @Test
    void deleteOthers() {
        var reservation = createReservationRequest(loginToken, request);

        var otherUser = memberRequest(Role.USER, "otherUser");
        createMemberRequest(otherUser);
        var othersToken = getLoginTokenRequest(tokenRequest(otherUser));

        var response = RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + othersToken)
            .when().delete(reservation.header("Location"))
            .then().log().all()
            .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("중복 예약을 생성한다")
    @Test
    void createDuplicateReservation() {
        createReservationRequest(loginToken, request);

        var response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + loginToken)
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
                .header("Authorization", "Bearer " + loginToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
