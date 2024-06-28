package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberTest {
    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    @Test
    @DisplayName("MemberController - create()")
    void 사용자_생성() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        String password = "password";

        var response = RestAssured
                .given().log().all()
                .body(new MemberRequest(name, email, password))
                .contentType(ContentType.JSON)
                .when().post("/members")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        MemberResponse body = response.body().as(MemberResponse.class);
        assertThat(body.name()).isEqualTo(name);
        assertThat(body.email()).isEqualTo(email);
    }

    @Test
    @DisplayName("MemberController - create() duplicated email")
    void 이미_사용하고_있는_이메일로_사용자_생성() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        String password = "password";
        사용자_생성();

        var response = RestAssured
                .given().log().all()
                .body(new MemberRequest(name, email, password))
                .contentType(ContentType.JSON)
                .when().post("/members")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
