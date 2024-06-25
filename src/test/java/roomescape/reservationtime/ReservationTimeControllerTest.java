package roomescape.reservationtime;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
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
import roomescape.globalfixture.dto.ReservationTimeDtoFixture;
import roomescape.reservationtime.dto.ReservationTimeRequestDto;
import roomescape.reservationtime.dto.ReservationTimeResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
    }

    @DisplayName("전체 예약을 조회 합니다.")
    @Test
    void getTimes() {
        //when
        final Response response = RestAssured
                .given().log().all()
                .when().get("/times")
                .then().log().all().extract().response();
        List<ReservationTimeResponseDto> reservations = response.as(new TypeRef<List<ReservationTimeResponseDto>>() {});

        // then
        assertSoftly(
                softAssertions ->{
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                    assertThat(reservations.size()).isEqualTo(3);
                }
        );
    }

    @DisplayName("시간 추가할 수 있습니다.")
    @Test
    void addTime() {
        // given
        final ReservationTimeRequestDto request = ReservationTimeDtoFixture.createReservationTimeRequestDto();

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all().extract().response();
        final ReservationTimeResponseDto responseDto = response.as(ReservationTimeResponseDto.class);

        // then
        assertSoftly(
                softAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                    assertThat(responseDto.getStartAt()).isEqualTo(request.getStartAt());
                }
        );
    }

    @DisplayName("시간을 삭제할 수 있습니다.")
    @Test
    void deleteTime() {
        // given
        final Response response = createTime(ReservationTimeDtoFixture.createReservationTimeRequestDto());
        final ReservationTimeResponseDto responseDto = response.as(ReservationTimeResponseDto.class);

        // when
        final Response response2 = RestAssured.given().log().all()
                .when().delete("/times/" + responseDto.getId())
                .then().log().all().extract().response();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @DisplayName("시간 형식이 적절하지 못하면 예외가 발생합니다.")
    @Test
    void addTimeException() {
        // given
        final ReservationTimeRequestDto request = new ReservationTimeRequestDto("30:40");

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all().extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("시간이 null 이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void addTimeException3(final String startAt) {
        // given
        final ReservationTimeRequestDto request = new ReservationTimeRequestDto(startAt);

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all().extract().response();
        final ReservationTimeRequestDto reservationTimeRequestDto = response.as(ReservationTimeRequestDto.class);

        // then
        assertSoftly(
                softAssertions -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                    assertThat(reservationTimeRequestDto.getStartAt()).isEqualTo("예약 시간을 입력해주세요");
                }
        );
    }

    private Response createTime(ReservationTimeRequestDto request){
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all().extract().response();
    }
}
