package nextstep.acceptance;

import io.restassured.RestAssured;
import nextstep.application.dto.auth.AuthRequest;
import nextstep.application.dto.auth.AuthResponse;
import nextstep.application.dto.member.MemberRequest;
import nextstep.application.service.member.MemberCommandService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthE2ETest {

    @Autowired
    private MemberCommandService memberCommandService;

    @BeforeEach
    void setUp() {
        MemberRequest body = new MemberRequest("dani", "dani", "다니", "010-1234-5678");

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @AfterEach
    void tearDown() {
        memberCommandService.deleteAll();
    }

    @DisplayName("토큰을 생성한다.")
    @Test
    void create() {
        AuthRequest body = new AuthRequest("dani", "dani");

        var response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        Assertions.assertThat(response.as(AuthResponse.class)).isNotNull();
    }
}
