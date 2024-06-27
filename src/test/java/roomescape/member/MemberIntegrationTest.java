package roomescape.member;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import roomescape.globalfixture.dto.LoginMemberDtoFixture;
import roomescape.member.dto.LoginMemberRequestDto;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("로그인을 하면 토큰을 응답받습니다.")
    @Test()
    void basicLogin() {
        //given
        final LoginMemberRequestDto loginMemberRequestDto = LoginMemberDtoFixture.createLoginMember();

        // when, then
        RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .assertThat()
                .cookie("token", notNullValue());
    }

    @DisplayName("잘못된 계정정보로 로그인 요청을 하면 예외가 발생합니다.")
    @ParameterizedTest
    @CsvSource(value = {"json@email.com, 1234!@#$", "error@error.com, 1234"})
    void basicLoginException(String email, String password) {
        //given
        final LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(email, password);

        //when
        final Response response = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().response();
        String responseBody = response.getBody().asString();

        //then
        assertThat(responseBody).isEqualTo("Authorization Exception: 해당하는 회원 정보가 없습니다.");
    }

    @DisplayName("쿠키를 이용하여 로그인 사용자 이름을 응답받습니다.")
    @ParameterizedTest
    @CsvSource(value = {"json@email.com, 1234"})
    void getMemberNameUsingCookie(final String email, final String password) {
        //given
        final LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(email, password);
        final Response response = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().response();
        final String token = response.cookie("token");

        //when
        final Response actual = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all().extract().response();

        //then
        assertThat(actual.jsonPath().getString("name")).isEqualTo("제이슨");
    }
}
