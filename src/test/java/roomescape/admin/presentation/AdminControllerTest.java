package roomescape.admin.presentation;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.Map;

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

    @Test
    void 어드민_계정으로_예약을_한다() {
        // given
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");

        String accessToken = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        String date = LocalDate.now().plusDays(1).toString();
        ReservationCreateRequest request = new ReservationCreateRequest(date, 1L, 1L, 1L);

        // when & then
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 사용자_계정으로_어드민_페이지에_접근하면_예외가_발생한다() {
        // given
        LoginRequest loginRequest = new LoginRequest("user@email.com", "password");

        String accessToken = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        // when & then
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 예약을_검색한다() {
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");

        String accessToken = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        Map<String, String> params = Map.of(
                "userId", "1",
                "themeId", "1",
                "dateFrom", "2024-06-01",
                "dateTo", "2024-06-30"
        );

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .params(params)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }
}
