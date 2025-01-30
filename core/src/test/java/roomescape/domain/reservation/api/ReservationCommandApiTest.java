package roomescape.domain.reservation.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.common.exception.CustomErrorCode;
import roomescape.fixture.LoginFixture;
import roomescape.support.RestAssuredTestSupport;

import static org.hamcrest.Matchers.equalTo;

class ReservationCommandApiTest extends RestAssuredTestSupport {

    @Autowired
    LoginFixture loginFixture;

    @DisplayName("예약 삭제 시 해당 예약이 존재하지 않는 경우 404를 응답한다")
    @Test
    void delete_fail() {
        // given
        final long nonExistingReservationId = -1L;

        String accessToken = loginFixture.loginWithUserName("홍길동");

        // when & then
        RestAssured.given().log().all()
                .cookie("accessToken", accessToken)
                .when().pathParam("reservationId", nonExistingReservationId)
                .post("/api/reservations/{reservationId}/cancel")
                .then().log().all()
                .statusCode(404)
                .body("statusCode", equalTo(404))
                .body("httpStatus", equalTo("NOT_FOUND"))
                .body("data.errorCode", equalTo(CustomErrorCode.R404.name()));
    }
}
