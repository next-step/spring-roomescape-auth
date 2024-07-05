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
import roomescape.reservationtime.application.ReservationTimeService;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.theme.application.ThemeService;
import roomescape.theme.ui.dto.ThemeRequest;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationDeleteTest {
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

    @Test
    @DisplayName("예약 취소")
    void cancelReservation() {
        String token = createToken();
        String date = LocalDate.now().plusWeeks(1).toString();
        reservationTimeService.add(ReservationTimeRequest.create("15:00"));
        themeService.add(ThemeRequest.of("theme1", "bla", ""));
        reservationService.make(ReservationRequest.of(1L, date, 1L, 1L));

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 예약 취소")
    void failToCancelNonExistentReservation() {
        String token = createToken();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
