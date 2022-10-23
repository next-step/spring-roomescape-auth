package nextstep;

import io.restassured.RestAssured;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class E2ETest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "bada";
    public static final String PHONE = "010-1234-5678";
    public static final String ROLE = "ADMIN";
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    protected TokenResponse token;

    @BeforeEach
    protected void setUp() {
        MemberRequest memberBody = new MemberRequest(USERNAME, PASSWORD, NAME, PHONE, ROLE);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all();

        TokenRequest tokenBody = new TokenRequest(USERNAME, PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .extract();

        token = response.as(TokenResponse.class);
    }
}
