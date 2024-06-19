package roomescape.member;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import roomescape.member.dto.LoginMemberRequestDto;
import roomescape.member.dto.MemberResponseDto;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberControllerTest {

    private static final String EMAIL = "json@email.com";
    private static final String PASSOWORD = "1234";

    @DisplayName("로그인을 하면 토큰을 응답받는다.")
    @Test
    void basicLogin(){

        final LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(EMAIL, PASSOWORD);
        var response = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract();

        assertThat(response.cookie("token")).isNotEmpty();

    }

    @DisplayName("잘못된 계정정보로 로그인 요청을 하면 예외가 발생한다.")
    @Test
    void basicLoginException1(){

        final LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(EMAIL, "4312");
        var response = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().response();

        // 응답 바디를 문자열로 추출 및 출력
        String responseBody = response.getBody().asString();

        // 응답 바디를 직접 비교
        assertThat(responseBody).isEqualTo("Authorization Exception: 해당하는 회원 정보가 없습니다.");

        final LoginMemberRequestDto loginMemberRequestDto2 = new LoginMemberRequestDto("test@test.com", PASSOWORD);
        var response2 = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().response();

        // 응답 바디를 문자열로 추출 및 출력
        String responseBody2 = response.getBody().asString();

        // 응답 바디를 직접 비교
        assertThat(responseBody2).isEqualTo("Authorization Exception: 해당하는 회원 정보가 없습니다.");

    }

    @DisplayName("쿠키를 이용하여 로그인 사용자 이름을 받아온다.")
    @Test
    void getMemberNameUsingCookie() {
        //given
        final LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(EMAIL, PASSOWORD);
        var response = RestAssured
                .given().log().all()
                .body(loginMemberRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract();

        String token = response.cookie("token");

        MemberResponseDto memberResponseDto = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all().extract().as(MemberResponseDto.class);

        assertThat(memberResponseDto.getName()).isEqualTo("제이슨");
    }

}
