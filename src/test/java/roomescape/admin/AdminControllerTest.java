package roomescape.admin;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.dto.LoginRequest;
import roomescape.reservation.dto.ReservationCreateRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {

    private String accessToken;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");

        accessToken = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    @Test
    void 어드민_계정으로_예약을_한다() {
        String date = LocalDate.now().plusDays(1).toString();
        ReservationCreateRequest request = new ReservationCreateRequest(date, 1L, 1L, 1L);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
