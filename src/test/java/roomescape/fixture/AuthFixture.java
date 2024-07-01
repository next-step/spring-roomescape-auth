package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import roomescape.dto.request.LoginRequest;

public class AuthFixture {

    public static Response 로그인(String email, String password) {
        return RestAssured
                .given().log().all()
                .body(new LoginRequest(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login");
    }
}
