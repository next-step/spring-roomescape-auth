package roomescape.application.domain.theme.service.command;

import roomescape.domain.theme.vo.ThemeId;

public class DeleteThemeCommand {

    private final Long themeId;

    public DeleteThemeCommand(Long themeId) {
        this.themeId = themeId;
    }

    public ThemeId toThemeId() {
        return new ThemeId(themeId);
    }
}
