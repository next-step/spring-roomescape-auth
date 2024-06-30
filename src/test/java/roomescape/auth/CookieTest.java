package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.application.CookieUtils;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CookieTest {
    @Autowired
    private CookieUtils cookieUtils;

    Cookie[] makeDummyCookies() {
        Cookie[] cookies = new Cookie[5];

        for (int i = 0; i < 5; i++) {
            cookies[i] = cookieUtils.createCookie(String.valueOf(i), String.valueOf(i * 100));
        }
        return cookies;
    }

    @Test
    @DisplayName("쿠키 생성")
    void createCookie() {
        String name = "name";
        String value = "yeeun";

        Cookie cookie = cookieUtils.createCookie(name, value);

        assertThat(cookie.getName()).isEqualTo(name);
        assertThat(cookie.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("쿠키 배열로부터 특정 쿠키 가져오기")
    void getCookieFromCookies() {
        Cookie[] cookies = makeDummyCookies();

        Cookie cookie = cookieUtils.getCookieByName(cookies, "1").get();

        assertThat(cookie.getName()).isEqualTo("1");
        assertThat(cookie.getValue()).isEqualTo("100");
    }
}
