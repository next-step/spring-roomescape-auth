package roomescape.member.application;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoder {
    public String encode(String password) {
        return Base64.encodeBase64String(password.getBytes());
    }
}
