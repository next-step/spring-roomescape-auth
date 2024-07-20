package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.reservation.create.ReservationCreateRequest;
import roomescape.dto.theme.create.ThemeCreateRequest;
import roomescape.dto.time.ReservationTimeRequest;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ThemeService themeService;
    @Autowired
    ReservationTimeService reservationTimeService;

    @BeforeEach
    void init() {
        reservationService.createReservation(new ReservationCreateRequest("2024-07-23", "brown", 1L, 1L));
    }

    @Test
    void create() {
        var response = RestAssured
                .given().log().all()
                .body(new ReservationCreateRequest("2024-07-23", "hardy", 1L,1L))
                .contentType(ContentType.JSON)
                .when().post("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void list() {
        var response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void delete() {
        var response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("reservations/1")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}