package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import roomescape.dto.request.MemberRequest;

public class MemberFixture {

    public static Response 회원가입(String email, String password, String name) {
        return RestAssured
                .given().log().all()
                .body(new MemberRequest(name, email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members");
    }
}
