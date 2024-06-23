package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import roomescape.GloblaFixture.dto.ReservationDtoFixture;
import roomescape.GloblaFixture.dto.ReservationThemeDtoFixture;
import roomescape.GloblaFixture.dto.ReservationTimeDtoFixture;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservationTheme.dto.ReservationThemeRequestDto;
import roomescape.reservationTime.dto.ReservationTimeRequestDto;
import roomescape.reservationTime.dto.ReservationTimeResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @DisplayName("예약을 생성합니다.")
    @Test
    void createReservation() {
        // given
        final ReservationRequestDto request = ReservationDtoFixture.createReservationDto();

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();
        final ReservationResponseDto responseDto = response.as(ReservationResponseDto.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseDto.getName()).isEqualTo(request.getName());
        assertThat(responseDto.getDate()).isEqualTo(request.getDate());
        assertThat(responseDto.getReservationTimeResponseDto().getStartAt())
                .isEqualTo(request.getReservationTimeRequestDto().getStartAt());
    }

    @DisplayName("전체 예약을 조회 합니다.")
    @Test
    void readReservation() {
        var response = RestAssured
                .given().log().all()
                .when().get("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", ReservationResponseDto.class)).hasSize(1);
    }

    @DisplayName("예약을 삭제합니다.")
    @Test
    void deleteReservation() {
        // given
        final ReservationRequestDto request = ReservationDtoFixture.createReservationDto();

        // when
        final Response response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // when
        final Response response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().delete("/reservations/" + response1.as(ReservationResponseDto.class).getId())
                .then().log().all().extract().response();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("예약자 명이 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createReservationEmptyName(final String name) {
        // given
        final ReservationTimeRequestDto reservationTimeRequestDto = ReservationTimeDtoFixture.createReservationTimeRequestDto();
        final ReservationThemeRequestDto reservationThemeRequestDto = ReservationThemeDtoFixture.createReservationThemeDto();
        final ReservationRequestDto request = new ReservationRequestDto(name, "2025-12-25", reservationTimeRequestDto, reservationThemeRequestDto);

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("예약자 명 입력해주세요");
    }

    @DisplayName("예약일자가 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createReservationEmptyDate(final String date) {
        // given
        final ReservationTimeRequestDto reservationTimeRequestDto = ReservationTimeDtoFixture.createReservationTimeRequestDto();
        final ReservationThemeRequestDto reservationThemeRequestDto = ReservationThemeDtoFixture.createReservationThemeDto();
        final ReservationRequestDto request = new ReservationRequestDto("김준성", date, reservationTimeRequestDto, reservationThemeRequestDto);

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("date")).isEqualTo("예약일자를 입력해주세요");
    }

    @DisplayName("특정 일자와 테마 선택 시 기 예약된 시간을 제외한 시간을 반환한다.")
    @Test
    void getAvaliableTimes() {
        // given
        final String date = "2024-12-25";
        final Long themeId = 1l;

        final String date2 = "2025-12-31";
        final Long themeId2 = 2l;

        // when
        final Response response = RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .when().get("/times/available")
                .then().log().all().extract().response();

        final Response response2 = RestAssured.given().log().all()
                .queryParam("date", date2)
                .queryParam("themeId", themeId2)
                .when().get("/times/available")
                .then().log().all().extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", ReservationTimeResponseDto.class)).hasSize(2);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.jsonPath().getList(".", ReservationTimeResponseDto.class)).hasSize(3);
    }
}
