package roomescape.web.controller.dto;

public final class CreateReservationRequest {

	private final String date;

	private final long timeId;

	private final long themeId;

	private final String memberName;

	private CreateReservationRequest(Builder builder) {
		this.date = builder.date;
		this.timeId = builder.timeId;
		this.themeId = builder.themeId;
		this.memberName = builder.memberName;
	}

	public String getDate() {
		return this.date;
	}

	public long getTimeId() {
		return this.timeId;
	}

	public long getThemeId() {
		return this.themeId;
	}

	public String getMemberName() {
		return this.memberName;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String date;

		private long timeId;

		private long themeId;

		private String memberName;

		public Builder date(String date) {
			this.date = date;
			return this;
		}

		public Builder timeId(long timeId) {
			this.timeId = timeId;
			return this;
		}

		public Builder themeId(long themeId) {
			this.themeId = themeId;
			return this;
		}

		public Builder memberName(String memberName) {
			this.memberName = memberName;
			return this;
		}

		public CreateReservationRequest build() {
			return new CreateReservationRequest(this);
		}

	}

}
