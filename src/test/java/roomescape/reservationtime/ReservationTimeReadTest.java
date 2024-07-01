package roomescape.reservationtime;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.ui.dto.ReservationRequest;
import roomescape.reservationtime.application.ReservationTimeService;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.theme.application.ThemeService;
import roomescape.theme.ui.dto.ThemeRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeReadTest {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ThemeService themeService;

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    @Test
    @DisplayName("전체 예약 시간 조회")
    void readAllReservationTimes() {
        reservationTimeService.add(ReservationTimeRequest.create("13:00"));
        reservationTimeService.add(ReservationTimeRequest.create("15:00"));

        var response = RestAssured
                .given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", ReservationTimeResponse.class)).hasSize(2);
    }

    @Test
    @DisplayName("예약_시간_하나도_없는_경우_전체_예약_시간_조회")
    void readAllReservationsIfNoReservationTimes() {
        var response = RestAssured
                .given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", ReservationTimeResponse.class)).hasSize(0);
    }

    @Test
    @DisplayName("예약 가능 시간 조회")
    void readBookableReservationTime() {
        themeService.add(ThemeRequest.of("theme1", "b", "c"));
        themeService.add(ThemeRequest.of("theme2", "b", "c"));
        reservationTimeService.add(ReservationTimeRequest.create("12:00"));
        reservationTimeService.add(ReservationTimeRequest.create("14:00"));
        reservationTimeService.add(ReservationTimeRequest.create("16:00"));
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        reservationService.make(ReservationRequest.of("yeeun", tomorrow.toString(), 1L, 1L));
        reservationService.make(ReservationRequest.of("red", tomorrow.toString(), 2L, 2L));
        reservationService.make(ReservationRequest.of("joy", tomorrow.toString(), 3L, 2L));
        reservationService.make(ReservationRequest.of("pobi", tomorrow.plusDays(1).toString(), 1L, 1L));

        var response1 = RestAssured
                .given().log().all()
                .when().get("/times/available?date=" + tomorrow.toString() + "&themeId=1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response1.jsonPath().getList("", ReservationTimeResponse.class)).hasSize(2);

        var response2 = RestAssured
                .given().log().all()
                .when().get("/times/available?date=" + tomorrow.toString() + "&themeId=2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response2.jsonPath().getList("", ReservationTimeResponse.class)).hasSize(1);
    }

    @Test
    @DisplayName("예약시간 하나 조회")
    void readReservationTime() {
        String startAt = "13:00";
        reservationTimeService.add(ReservationTimeRequest.create(startAt));

        var reservationTime = RestAssured
                .given().log().all()
                .when().get("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ReservationTimeResponse.class);

        assertThat(reservationTime.startAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 id로 예약시간 하나 조회")
    void failToReadNonExistentReservationTime() {
        RestAssured
                .given().log().all()
                .when().get("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
