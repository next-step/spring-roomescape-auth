package roomescape.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.theme.dto.ThemeRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static roomescape.step.LoginStep.*;
import static roomescape.step.ReservationStep.*;
import static roomescape.step.ReservationTimeStep.예약_시간_등록;
import static roomescape.step.ThemeStep.*;

@DisplayName("테마 관련 api 호출 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeAcceptanceTest {

    private String adminToken;

    @BeforeEach
    void 테마_등록_성공() {
        ThemeRequest request = new ThemeRequest("테스트 테마", "이것은 테스트용 테마 입니다.", "thumbailName");
        adminToken = 관리자_토큰_생성();

        ExtractableResponse<Response> response = 테마_등록(adminToken ,request);
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void 관리자_외_테마_등록_실패() {
        ThemeRequest request = new ThemeRequest("테스트 테마", "이것은 테스트용 테마 입니다.", "thumbailName");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .cookie("token", 회원_토큰_생성())
            .body(request)
            .when().post("/themes")
            .then().log().all()
            .statusCode(401);
    }

    @Test
    void 테마_조회_성공() {
        RestAssured.given().log().all()
            .when().get("/themes")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(1));
    }


    @Test
    void 테마_삭제_성공() {
        RestAssured.given().log().all()
            .cookie("token", adminToken)
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(204);
    }

    @Test
    void 예약이_존재하는_테마_삭제_실패() {
        예약_시간_등록(new ReservationTimeRequest("12:00"));

        예약_등록(new ReservationRequest("2025-08-05", 1L, 1L));

        RestAssured.given().log().all()
            .cookie("token", adminToken)
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(409);
    }

    @Test
    void 관리자_외_테마_삭제_실패() {
        RestAssured.given().log().all()
            .cookie("token", 회원_토큰_생성())
            .when().delete("/themes/1")
            .then().log().all()
            .statusCode(401);
    }
}
