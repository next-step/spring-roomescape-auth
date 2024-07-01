package roomescape.auth.ui.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginMember(
        @NotNull Long id,
        @NotBlank String name
) {
}
