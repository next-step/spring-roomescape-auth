package roomescape.reservationtime;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservationtime.application.ReservationTimeService;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.reservationtime.ui.dto.ReservationTimeResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeCreateTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    @Test
    @DisplayName("예약 시간 생성")
    void createReservationTime() {
        String startAt = "13:00";

        var body = RestAssured
                .given().log().all()
                .body(ReservationTimeRequest.create(startAt))
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(ReservationTimeResponse.class);

        assertThat(body.startAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("예외 - 이미 존재하는 예약 시간 생성")
    void failToCreateIfTimeAlreadyExist() {
        String startAt = "13:00";
        reservationTimeService.add(ReservationTimeRequest.create(startAt));

        var response = RestAssured
                .given().log().all()
                .body(ReservationTimeRequest.create(startAt))
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    @ParameterizedTest
    @DisplayName("예외 - 유효하지 않은 시작 시간으로 예약 시간 생성")
    @ValueSource(strings = {"asdf", "24:24", "", "24:00"})
    void failToCreateIfStartTimeIsInvalid(String startAt) {
        RestAssured
                .given().log().all()
                .body(ReservationTimeRequest.create(startAt))
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
