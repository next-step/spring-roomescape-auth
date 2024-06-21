package roomescape.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

	private String tokenSecretKey;

	private long tokenExpireLength;

	private long clockSkewSeconds;

	public String getTokenSecretKey() {
		return this.tokenSecretKey;
	}

	public void setTokenSecretKey(String tokenSecretKey) {
		this.tokenSecretKey = tokenSecretKey;
	}

	public long getTokenExpireLength() {
		return this.tokenExpireLength;
	}

	public void setTokenExpireLength(long tokenExpireLength) {
		this.tokenExpireLength = tokenExpireLength;
	}

	public long getClockSkewSeconds() {
		return this.clockSkewSeconds;
	}

	public void setClockSkewSeconds(long clockSkewSeconds) {
		this.clockSkewSeconds = clockSkewSeconds;
	}

}
