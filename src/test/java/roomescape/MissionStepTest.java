package roomescape;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.dto.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

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
    void page() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void AdminReservationPage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void timePage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void themePage() {
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void reservationPage() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void loginPage() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }
}
