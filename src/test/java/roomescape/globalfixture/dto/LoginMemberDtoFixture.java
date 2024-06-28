package roomescape.globalfixture.dto;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.member.dto.LoginMemberRequestDto;

public class LoginMemberDtoFixture {

    public static LoginMemberRequestDto createLoginMember(){
        return new LoginMemberRequestDto("json@email.com", "1234");
    }

    public static String getLoginMemberToken() {
        final LoginMemberRequestDto loginMember = LoginMemberDtoFixture.createLoginMember();
        final String token = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(loginMember)
                .when().post("/login")
                .then().log().all().extract().cookie("token");
        return token;
    }
}
