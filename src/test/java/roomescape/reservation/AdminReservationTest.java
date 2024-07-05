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
import roomescape.reservation.ui.dto.AdminReservationRequest;

import roomescape.reservationtime.application.ReservationTimeService;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.theme.application.ThemeService;
import roomescape.theme.ui.dto.ThemeRequest;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminReservationTest {
    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ThemeService themeService;

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    private String createAdminToken() {
        String name = "admin";
        String email = "admin@gmail.com";
        String password = "password";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new MemberRequest(name, email, password))
                .when().post("/members?role=ADMIN")
                .then().extract().body().as(MemberResponse.class);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().extract().cookie("token");
    }

    private void makeDummyTimeAndTheme() {
        reservationTimeService.add(ReservationTimeRequest.create("13:00"));
        themeService.add(ThemeRequest.of("theme1", "bla", "thumbnail"));
    }

    @Test
    @DisplayName("관리자 페이지에서 예약 생성")
    void reserveInAdminPage() {
        String token = createAdminToken();
        makeDummyTimeAndTheme();
        String date = LocalDate.now().plusWeeks(1).toString();

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(new AdminReservationRequest(date, 1L, 1L, 1L))
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
