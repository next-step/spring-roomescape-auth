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
import roomescape.theme.application.ThemeService;
import roomescape.theme.ui.dto.ThemeRequest;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeDeleteTest {
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

    private long makeDummyReservation() {
        long timeId = reservationTimeService.add(ReservationTimeRequest.create("13:00")).id();
        long themeId = themeService.add(ThemeRequest.of("a", "b", "c")).id();
        String date = LocalDate.now().plusWeeks(1).toString();
        return reservationService.make(ReservationRequest.of("yeeun", date, timeId, themeId)).id();
    }

    @Test
    @DisplayName("예약 시간 삭제")
    void deleteReservationTime() {
        reservationTimeService.add(ReservationTimeRequest.create("13:00"));

        RestAssured
                .given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 예약 시간 삭제")
    void failToDeleteNonExistentReservationTime() {
        RestAssured
                .given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("에외 - 예약이 되어있는 예약 시간 삭제")
    void failToDeleteIfReservationWithTimeExist() {
        long reservationId = makeDummyReservation();

        RestAssured
                .given().log().all()
                .when().delete("/times/" + reservationId)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }
}
