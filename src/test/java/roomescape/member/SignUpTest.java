package roomescape.member;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.application.SignUpService;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SignUpTest {
    @Autowired
    private SignUpService signUpService;

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    @Test
    @DisplayName("회원가입 및 location 헤더로 자원 접근 확인")
    void 사용자_생성() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        String password = "password";

        var response = RestAssured
                .given().log().all()
                .body(new MemberRequest(name, email, password))
                .contentType(ContentType.JSON)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        MemberResponse body = response.body().as(MemberResponse.class);
        assertThat(body.name()).isEqualTo(name);
        assertThat(body.email()).isEqualTo(email);
        String location = response.header("location");
        assertThat(location).isEqualTo("/members/" + body.id());

//        location 헤더 확인
        MemberResponse member = RestAssured
                .given().log().all()
                .when().get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberResponse.class);

        assertThat(member.name()).isEqualTo(name);
        assertThat(member.email()).isEqualTo(email);
    }

    @Test
    @DisplayName("예외 - 중복 이메일로 회원가입")
    void 이미_사용하고_있는_이메일로_사용자_생성() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        String password = "password";
        signUpService.signUp(new MemberRequest(name, email, password));

        RestAssured
                .given().log().all()
                .body(new MemberRequest(name, email, password))
                .contentType(ContentType.JSON)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
