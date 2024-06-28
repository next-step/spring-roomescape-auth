package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.globalfixture.dto.LoginMemberDtoFixture;
import roomescape.globalfixture.dto.ReservationDtoFixture;
import roomescape.reservation.dto.ReservationRequestDto;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationIntegrationTest {

    private ReservationRequestDto reservationRequestDto;
    private String token;

    @BeforeEach
    void setUp(){
        reservationRequestDto = ReservationDtoFixture.createReservationDto();
        token = LoginMemberDtoFixture.getLoginMemberToken();
    }

    @DisplayName("저장된 시간과 테마를 기준으로 예약 등록 및 조회합니다.")
    @Test
    void reservationCreate() {
        //given
        saveReservation();

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("저장된 예약을 삭제합니다.")
    @Test
    void reservationDelete(){
        // 예약 저장
        saveReservation();

        // 예약 삭제
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        // 예약 조회
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    private void saveReservation() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationRequestDto)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1));
    }
}
