package roomescape.domain;

public class LoginMember {

	private String name;

	private String email;

	private String role;

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}

	public String getRole() {
		return this.role;
	}

	@Override
	public String toString() {
		// @formatter:off
		return "LoginMember{" +
				"name='" + this.name + '\'' +
				", email='" + this.email + '\'' +
				", role='" + this.role + '\'' +
				'}';
		// @formatter:on
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private final LoginMember loginMember;

		public Builder() {
			this.loginMember = new LoginMember();
		}

		public Builder name(String name) {
			this.loginMember.name = name;
			return this;
		}

		public Builder email(String email) {
			this.loginMember.email = email;
			return this;
		}

		public Builder role(String role) {
			this.loginMember.role = role;
			return this;
		}

		public LoginMember build() {
			return this.loginMember;
		}

	}

}
