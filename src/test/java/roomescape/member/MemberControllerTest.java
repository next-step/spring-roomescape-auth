package roomescape.member;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import roomescape.member.dto.LoginMemberRequestDto;
import roomescape.member.dto.MemberResponseDto;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberControllerTest {

    private static final String EMAIL = "json@email.com";
    private static final String PASSOWORD = "1234";

    @DisplayName("로그인을 하면 토큰을 응답받습니다.")
    @Test
    void basicLogin() {
        //given
        final LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(EMAIL, PASSOWORD);

        // when
        RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .assertThat()
                .cookie("token", notNullValue());


    }

    @DisplayName("잘못된 계정정보로 로그인 요청을 하면 예외가 발생합니다.")
    @Test
    void basicLoginException1() {
        //given1
        final LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(EMAIL, "4312");

        //when1
        final Response response = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().response();
        String responseBody = response.getBody().asString();

        //then1
        assertThat(responseBody).isEqualTo("Authorization Exception: 해당하는 회원 정보가 없습니다.");

        //given2
        final LoginMemberRequestDto loginMemberRequestDto2 = new LoginMemberRequestDto("test@test.com", PASSOWORD);

        //when2
        final Response response2 = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().response();
        final String responseBody2 = response2.getBody().asString();

        //then2
        assertThat(responseBody2).isEqualTo("Authorization Exception: 해당하는 회원 정보가 없습니다.");
    }

    @DisplayName("쿠키를 이용하여 로그인 사용자 이름을 응답받습니다.")
    @Test
    void getMemberNameUsingCookie() {
        //given
        final LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(EMAIL, PASSOWORD);
        final Response response = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().response();
        final String token = response.cookie("token");

        //when
        final MemberResponseDto memberResponseDto = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all().extract().as(MemberResponseDto.class);

        //then
        assertThat(memberResponseDto.getName()).isEqualTo("제이슨");
    }
}
