package roomescape;

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
public class ReservationTest {
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

    public String createToken() {
        String email = "anna862700@gmail.com";
        String password = "password";

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberRequest(NAME, email, password))
                .when().post("/members")
                .then().log().all().extract().body().as(MemberResponse.class);
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().log().all().extract().cookie("token");
    }

    private void makeDummyTimesAndThemes() {
        reservationTimeService.add(ReservationTimeRequest.create("13:00"));
        reservationTimeService.add(ReservationTimeRequest.create("15:00"));
        themeService.add(ThemeRequest.create("theme1", "bla", ""));
        themeService.add(ThemeRequest.create("theme2", "fun", ""));
    }

    @Test
    @DisplayName("ReservationController - create()")
    void 예약() {
        String date = LocalDate.now().plusWeeks(1).toString();
        String token = createToken();
        makeDummyTimesAndThemes();

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ReservationResponse body = response.body().as(ReservationResponse.class);
        assertThat(body.getMemberName()).isEqualTo(NAME);
        assertThat(body.getDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("ReservationController - create() date already past")
    void 과거_날짜_시간_예약() {
        String date = LocalDate.now().minusWeeks(1).toString();
        String token = createToken();
        makeDummyTimesAndThemes();

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("ReservationController - create() same-day")
    void 당일_예약() {
        String date = LocalDate.now().toString();
        String token = createToken();
        makeDummyTimesAndThemes();

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("ReservationController - create() not existent time")
    void 존재하지_않는_시간_예약() {
        String date = LocalDate.now().plusWeeks(1).toString();
        String token = createToken();
        themeService.add(ThemeRequest.create("a", "b", "c"));

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("ReservationController - create() not existent theme")
    void 존재하지_않는_테마_예약() {
        String date = LocalDate.now().plusWeeks(1).toString();
        String token = createToken();
        reservationTimeService.add(ReservationTimeRequest.create("13:00"));

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("ReservationController - create() duplicated date, time and theme")
    void 중복_예약() {
        String date = LocalDate.now().plusWeeks(1).toString();
        String token = createToken();
        makeDummyTimesAndThemes();
        reservationService.make(ReservationRequest.create("yeeun", date, 1L, 1L));

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(new CookieReservationRequest(date, 1L, 1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("ReservationController - read()")
    void 젼체_예약_조회() {
        String token = createToken();
        String date = LocalDate.now().plusWeeks(1).toString();
        makeDummyTimesAndThemes();
        reservationService.make(ReservationRequest.create("yeeun", date, 1L, 1L));

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("", ReservationResponse.class)).hasSize(1);
    }

    @Test
    @DisplayName("ReservationController - read() not existent reservation")
    void 예약이_없는_경우_예약_조회() {
        String token = createToken();

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("", ReservationResponse.class)).hasSize(0);
    }

    @Test
    @DisplayName("ReservationController - delete()")
    void 예약_취소() {
        String token = createToken();
        String date = LocalDate.now().plusWeeks(1).toString();
        makeDummyTimesAndThemes();
        reservationService.make(ReservationRequest.create("yeeun", date, 1L, 1L));

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/1")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("ReservationController - delete() : not existent reservation")
    void 존재하지_않는_예약_취소() {
        String token = createToken();

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/1")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
