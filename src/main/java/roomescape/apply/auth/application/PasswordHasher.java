package roomescape.apply.auth.application;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class PasswordHasher {

    private final MessageDigest digest;

    public PasswordHasher() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("PasswordHasher에 필요한 SHA-256 알고리즘이 존재하지 않습니다. ", e);
        }
    }

    public String hashPassword(String password) {
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        BigInteger hashNum = new BigInteger(1, hashBytes);
        StringBuilder hashedPassword = new StringBuilder(hashNum.toString(16));

        while (hashedPassword.length() < 64) {
            hashedPassword.insert(0, '0');
        }

        return hashedPassword.toString();
    }
}
