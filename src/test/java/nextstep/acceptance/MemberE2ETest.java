package nextstep.acceptance;

import io.restassured.RestAssured;
import nextstep.application.dto.member.MemberRequest;
import nextstep.application.service.member.MemberCommandService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberE2ETest {

    @Autowired
    private MemberCommandService memberCommandService;

    @AfterEach
    void tearDown() {
        memberCommandService.deleteAll();
    }

    @DisplayName("사용자를 생성한다.")
    @Test
    void create() {
        MemberRequest request = new MemberRequest("dani", "dani", "다니", "010-1234-5678");

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }
}
