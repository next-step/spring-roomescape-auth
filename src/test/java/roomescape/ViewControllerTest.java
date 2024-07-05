package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.ui.dto.LoginRequest;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ViewControllerTest {
    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    private String createAdminToken() {
        String name = "admin";
        String email = "admin@gmail.com";
        String password = "password";

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberRequest(name, email, password))
                .when().post("/members?role=ADMIN")
                .then()
                .extract().body().as(MemberResponse.class);
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then()
                .extract().cookie("token");
    }

    @ParameterizedTest
    @DisplayName("관리자 페이지 - 예약, 시간, 테마")
    @ValueSource(strings = {"reservation", "time", "theme"})
    void readAdminPage(String pageName) {
        String token = createAdminToken();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/admin/" + pageName)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @ParameterizedTest
    @DisplayName("인증 페이지 - 로그인, 회원가입")
    @ValueSource(strings = {"login", "signup"})
    void readLoginAndSignupPage(String pageName) {
        RestAssured
                .given().log().all()
                .when().get("/" + pageName)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("사용자 예약 페이지")
    void readReservationPage() {
        String token = createAdminToken();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
