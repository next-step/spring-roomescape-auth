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
import roomescape.reservation.ui.dto.CookieReservationRequest;
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
public class ReservationCreateTest {
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
    @DisplayName("예약 생성")
    void reserve() {
        String date = LocalDate.now().plusWeeks(1).toString();
        String token = createToken();
        makeDummyTimesAndThemes();

        var body = RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode((HttpStatus.CREATED.value()))
                .extract().as(ReservationResponse.class);

        assertThat(body.date()).isEqualTo(date);
    }

    @Test
    @DisplayName("예외 - 과거 날짜로 예약")
    void failToReserveIfDateIsPassed() {
        String date = LocalDate.now().minusWeeks(1).toString();
        String token = createToken();
        makeDummyTimesAndThemes();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("예외 - 당일 예약")
    void failToReserveIfDateIsToday() {
        String date = LocalDate.now().toString();
        String token = createToken();
        makeDummyTimesAndThemes();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("예외 - 존재하지 않은 시간id로 예약")
    void failToReserveIfReservationTimeMatchWithIdNotExist() {
        String date = LocalDate.now().plusWeeks(1).toString();
        String token = createToken();
        themeService.add(ThemeRequest.of("a", "b", "c"));

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("예외 - 존재하지 않은 테마id로 예약")
    void failToReserveIfThemeMatchWithIdNotExist() {
        String date = LocalDate.now().plusWeeks(1).toString();
        String token = createToken();
        reservationTimeService.add(ReservationTimeRequest.create("13:00"));

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode((HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @DisplayName("예외 - 중복 예약")
    void failToReserveIfAlreadyScheduled() {
        String date = LocalDate.now().plusWeeks(1).toString();
        String token = createToken();
        makeDummyTimesAndThemes();
        reservationService.make(ReservationRequest.of(1L, date, 1L, 1L));

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
