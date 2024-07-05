package roomescape.domain.user.vo;

public record UserId(Long id) {

    public static UserId empty() {
        return new UserId(null);
    }
}
