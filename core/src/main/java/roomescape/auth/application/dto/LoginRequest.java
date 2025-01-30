package roomescape.auth.application.dto;

public record LoginRequest(
        String email,
        String password
) {

}
