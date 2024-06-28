package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import roomescape.globalfixture.dto.*;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.TimeDto;
import roomescape.reservationtheme.dto.ReservationThemeRequestDto;
import roomescape.reservationtime.dto.ReservationTimeResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerTest {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = LoginMemberDtoFixture.getLoginMemberToken();
    }

    @DisplayName("예약을 생성합니다.")
    @Test
    void createReservation() {
        // given
        final ReservationRequestDto request = ReservationDtoFixture.createReservationDto();

        // when
        final Response response = RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then()
                .log().all().extract().response();
        // then
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.statusCode()),
                () -> assertEquals("제이슨", response.jsonPath().getString("member.name")),
                () -> assertEquals(request.getDate(), response.jsonPath().getString("date")),
                () -> assertEquals(request.getTimeDto().getStartAt(),
                        response.jsonPath().getString("time.startAt"))
        );
    }

    @DisplayName("전체 예약을 조회 합니다.")
    @Test
    void readReservation() {
        // when
        var response = RestAssured
                .given().log().all()
                .when().get("/reservations")
                .then().log().all().extract();
        // then
        assertSoftly(
                softAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.jsonPath().getList("id").size()).isEqualTo(1);
                }
        );
    }

    @DisplayName("예약을 삭제합니다.")
    @Test
    void deleteReservation() {
        // given
        final ReservationRequestDto request = ReservationDtoFixture.createReservationDto();
        final Response response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // when
        final Response response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().delete("/reservations/" + response1.jsonPath().getInt("id"))
                .then().log().all().extract().response();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("예약일자가 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createReservationEmptyDate(final String date) {
        // given
        final TimeDto timeDto = TimeDtoFixture.timeDtoCreate();
        final ReservationThemeRequestDto reservationThemeRequestDto = ReservationThemeDtoFixture.createReservationThemeDto();
        final ReservationRequestDto request = new ReservationRequestDto(null, date, timeDto, reservationThemeRequestDto);

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // then
        assertSoftly(
                softAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(response.jsonPath().getString("date")).isEqualTo("예약일자를 입력해주세요");
                }
        );
    }

    @DisplayName("특정 일자와 테마 선택 시 기 예약된 시간을 제외한 시간을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2024-12-25, 1, 2", "2025-12-31, 2, 3",})
    void getAvaliableTimes(String date, Long themeId, int size) {
        // when
        final Response response = RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .when().get("/times/available")
                .then().log().all().extract().response();
        // then
        assertSoftly(
                softAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                    assertThat(response.jsonPath().getList(".", ReservationTimeResponseDto.class)).hasSize(size);
                }
        );
    }

}
