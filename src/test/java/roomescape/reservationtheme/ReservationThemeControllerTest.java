package roomescape.reservationtheme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import roomescape.globalfixture.dto.ReservationThemeDtoFixture;
import roomescape.reservationtheme.dto.ReservationThemeRequestDto;
import roomescape.reservationtheme.dto.ReservationThemeResponseDto;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationThemeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
    }

    @DisplayName("모든 테마를 조회할 수 있습니다.")
    @Test
    void getThemes() {
        // when
        final ReservationThemeResponseDto[] reservationThemeResponseDtos = RestAssured.given().log().all()
                .when().get("/themes").then().log().all().extract().as(ReservationThemeResponseDto[].class);

        // then
        assertThat(reservationThemeResponseDtos).hasSize(2);
    }

    @DisplayName("테마를 저장할 수 있다.")
    @Test
    void createTheme() {
        //given
        final ReservationThemeRequestDto request = ReservationThemeDtoFixture.createReservationThemeDto();

        //when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all().extract().response();

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.jsonPath().getString("name")).isEqualTo(request.getName());
    }

    @DisplayName("테마를 삭제할 수 있습니다.")
    @Test
    void deleteTheme() {
        //given
        final ReservationThemeRequestDto request = ReservationThemeDtoFixture.createReservationThemeDto();
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all().extract().response();
        final ReservationThemeResponseDto reservationThemeResponseDto = response.as(ReservationThemeResponseDto.class);

        //when
        final Response response2 = RestAssured.given().log().all()
                .when().delete("/themes/" + reservationThemeResponseDto.getId())
                .then().log().all().extract().response();

        //then
        assertThat(response2.statusCode()).isEqualTo(204);
    }

    @DisplayName("테마 이름이 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeEmptyName(final String name) {
        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto(name,
                "게임을 시작하지~! (-_-)b", "https://soo1.com");

        //when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();

        final ReservationThemeResponseDto reservationThemeResponseDto = response.as(ReservationThemeResponseDto.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(reservationThemeResponseDto.getName()).isEqualTo("테마명을 입력 해주세요");
    }

    @DisplayName("테마 설명이 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeEmptyDescription(final String description) {
        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto("쏘우1",
                description, "https://soo1.com");

        //when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();

        final ReservationThemeResponseDto reservationThemeResponseDto = response.as(ReservationThemeResponseDto.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(reservationThemeResponseDto.getDescription()).isEqualTo("테마에 대한 설명을 입력해주세요");
    }

    @DisplayName("테마 썸네일이 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeEmptyThumbnail(final String thumbnail) {
        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto("쏘우1",
                "게임을 시작하지~! (-_-)b", thumbnail);

        //when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();
        final ReservationThemeResponseDto reservationThemeResponseDto = response.as(ReservationThemeResponseDto.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(reservationThemeResponseDto.getThumbnail()).isEqualTo("썸네일 url 을 입력해주세요");
    }
}
