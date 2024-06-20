package roomescape.member.ui.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password
) {
}
