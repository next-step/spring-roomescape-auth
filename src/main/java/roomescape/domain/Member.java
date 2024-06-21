package roomescape.domain;

import java.util.Objects;

public class Member {

	private Long id;

	private String name;

	private String email;

	private String password;

	private String role;

	public static Builder builder() {
		return new Builder();
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}

	public String getRole() {
		return this.role;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Member that = (Member) o;
		return Objects.equals(this.id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	@Override
	public String toString() {
		// @formatter:off
		return "Member{" +
				"id=" + this.id +
				", name='" + this.name + '\'' +
				", email='" + this.email + '\'' +
				", password='" + this.password + '\'' +
				", role='" + this.role + '\'' +
				'}';
		// @formatter:on
	}

	public static final class Builder {

		private final Member member;

		public Builder() {
			this.member = new Member();
		}

		public Builder id(long id) {
			this.member.id = id;
			return this;
		}

		public Builder name(String name) {
			this.member.name = name;
			return this;
		}

		public Builder email(String email) {
			this.member.email = email;
			return this;
		}

		public Builder password(String password) {
			this.member.password = password;
			return this;
		}

		public Builder role(String role) {
			this.member.role = role;
			return this;
		}

		public Member build() {
			return this.member;
		}

	}

}
