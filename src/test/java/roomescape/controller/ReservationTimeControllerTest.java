package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.theme.create.ThemeCreateRequest;
import roomescape.dto.time.ReservationTimeRequest;
import roomescape.service.ReservationTimeService;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Test
    void create() {
        var response = RestAssured
                .given().log().all()
                .body(new ReservationTimeRequest("19:00"))
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void findTimes() {
        var response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void delete() {
        var response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/1");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("예약 가능한 시간대 확인")
    void availableTime() {
        //theme 등록
        RestAssured
                .given().log().all()
                .body(new ThemeCreateRequest("무서운 이야기",
                        "너무 무서움ㄷㄷ너무 무서움ㄷㄷ너무 무서움ㄷㄷ너무 무서움ㄷㄷ너무 무서움ㄷㄷ너무 무서움ㄷㄷ", "GOOD"))
                .contentType(ContentType.JSON)
                .when().post("/themes")
                .then().log().all().extract();

        RestAssured
                .given().log().all()
                .body(new ReservationTimeRequest("13:00"))
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all().extract();

        String date = "2024-06-24";
        Long timeId = 1L;

        var response = RestAssured
                .given().log().all()
                .param("date", date)
                .param("themeId", timeId)
                .contentType(ContentType.JSON)
                .when().get("/times/available")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}