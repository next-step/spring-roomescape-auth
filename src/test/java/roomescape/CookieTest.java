package roomescape;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.auth.application.CookieUtils;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
    @DisplayName("cookieUtils - createCookie()")
    void 쿠키_생성하기() {
        String name = "name";
        String value = "yeeun";

        Cookie cookie = cookieUtils.createCookie(name, value);

        assertThat(cookie.getName()).isEqualTo(name);
        assertThat(cookie.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("cookieUtils - getCookieByName()")
    void 쿠키_배열에서_특정_쿠키_가져오기() {
        Cookie[] cookies = makeDummyCookies();

        Cookie cookie = cookieUtils.getCookieByName(cookies, "1").get();

        assertThat(cookie.getName()).isEqualTo("1");
        assertThat(cookie.getValue()).isEqualTo("100");
    }
}
