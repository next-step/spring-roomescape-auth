package roomescape.member.application;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoder {
    public String encode(String password) {
        byte[] decodedBytes = Base64.decodeBase64(password);
        return new String(decodedBytes);
    }
}
