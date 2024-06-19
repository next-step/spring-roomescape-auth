package roomescape.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("사용자 테스트")
public class MemberTest {

    @Test
    @DisplayName("회원가입 테스트")
    void signup() {

    }

    @Test
    @DisplayName("회원가입 시 중복된 아이디가 있는 경우 에러가 발생한다.")
    void signupDuplicateId() {

    }

    @Test
    @DisplayName("회원가입 시 올바르지 않은 값인 경우 에러가 발생한다.")
    void signupIncorrectValue() {

    }

    @Test
    @DisplayName("모든 회원 정보를 조회한다.(아이디, 이름)")
    void findAllMembers() {

    }
}
