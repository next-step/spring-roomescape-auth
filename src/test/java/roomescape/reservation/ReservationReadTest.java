package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.ui.dto.LoginRequest;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.ui.dto.ReservationRequest;
import roomescape.reservation.ui.dto.ReservationResponse;
import roomescape.reservationtime.application.ReservationTimeService;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.theme.application.ThemeService;
import roomescape.theme.ui.dto.ThemeRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationReadTest {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ThemeService themeService;
    private final String NAME = "yeeun";

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    private String createToken() {
        String email = "anna862700@gmail.com";
        String password = "password";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new MemberRequest(NAME, email, password))
                .when().post("/members")
                .then().extract().body().as(MemberResponse.class);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().extract().cookie("token");
    }

    private void makeDummyTimesAndThemes() {
        reservationTimeService.add(ReservationTimeRequest.create("13:00"));
        reservationTimeService.add(ReservationTimeRequest.create("15:00"));
        themeService.add(ThemeRequest.of("theme1", "bla", ""));
        themeService.add(ThemeRequest.of("theme2", "fun", ""));
    }

    @Test
    @DisplayName("전체 예약 조회")
    void readAllReservations() {
        String token = createToken();
        String date = LocalDate.now().plusWeeks(1).toString();
        makeDummyTimesAndThemes();
        reservationService.make(ReservationRequest.of(1L, date, 1L, 1L));

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", ReservationResponse.class)).hasSize(1);
    }

    @Test
    @DisplayName("예약이 하나도 없는 경우 전체 예약 조회")
    void readAllReservationsIfNoReservations() {
        String token = createToken();

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", ReservationResponse.class)).hasSize(0);
    }

    @Test
    @DisplayName("단일 예약 조회")
    void readReservation() {
        String token = createToken();
        String date = LocalDate.now().plusWeeks(1).toString();
        makeDummyTimesAndThemes();
        reservationService.make(ReservationRequest.of(1L, date,1L, 1L));

        var reservation = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ReservationResponse.class);

        assertThat(reservation.date()).isEqualTo(date);
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 id로 예약 조회")
    void failToReadNonExistentReservation() {
        String token = createToken();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
