package roomescape.theme;

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
import roomescape.member.application.SignUpService;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.ui.dto.ReservationRequest;
import roomescape.reservationtime.application.ReservationTimeService;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.theme.application.ThemeService;
import roomescape.theme.ui.dto.ThemeRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeDeleteTest {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private SignUpService signUpService;

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    private String createToken() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        String password = "password";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new MemberRequest(name, email, password))
                .when().post("/members")
                .then().extract().body().as(MemberResponse.class);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().extract().cookie("token");
    }

    private long makeDummyReservation() {
        long memberId = signUpService.signUp(new MemberRequest("yeeun", "asdf@asdf", "password"), "GUEST").id();
        long timeId = reservationTimeService.add(ReservationTimeRequest.create("13:00")).id();
        long themeId = themeService.add(ThemeRequest.of("a", "b", "c")).id();
        String date = LocalDate.now().plusWeeks(1).toString();
        return reservationService.make(ReservationRequest.of(memberId, date, timeId, themeId)).id();
    }

    @Test
    @DisplayName("테마 삭제")
    void deleteTheme() {
        String token = createToken();
        String name = "수키도키";
        String description = "흐르는 대로 살자 해파리처럼🪼";
        String thumbnail = "https://pbs.twimg.com/media/GApx6fjagAAkFsX.jpg";
        themeService.add(ThemeRequest.of(name, description, thumbnail));

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
   }

    @Test
    @DisplayName("예외 - 존재하지 않는 테마 삭제")
    void failToDeleteNotExistentTheme() {
        String token = createToken();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract();
    }

    @Test
    @DisplayName("예외 - 예약이 되어있는 테마 삭제")
    void failToDeleteIfReservationWithThemeExist() {
        String token = createToken();
        long reservationId = makeDummyReservation();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().delete("/themes/" + reservationId)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
   }
}
