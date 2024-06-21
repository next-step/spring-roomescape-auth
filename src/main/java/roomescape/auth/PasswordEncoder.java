package roomescape.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// TODO: Spring Security 로 개선 가능
public final class PasswordEncoder {

	private static final String ALGORITHM = "SHA-256";

	private PasswordEncoder() {
		throw new IllegalStateException("Utility class");
	}

	public static String encode(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
			byte[] encodedHash = digest.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(encodedHash);
		}
		catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException("SHA-256 algorithm not found", ex);
		}
	}

}
