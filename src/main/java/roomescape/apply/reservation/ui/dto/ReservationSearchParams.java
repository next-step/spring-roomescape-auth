package roomescape.apply.reservation.ui.dto;

import org.springframework.util.StringUtils;
import roomescape.support.checker.ReservationDateChecker;

public record ReservationSearchParams(
        Long themeId,
        Long memberId,
        String dateFrom,
        String dateTo
) {

    public ReservationSearchParams {
        validateValues(themeId, memberId, dateFrom, dateTo);
    }

    private void validateValues(Long themeId, Long memberId, String dateFrom, String dateTo) {
        if (themeId == null && memberId == null && !StringUtils.hasText(dateFrom) && !StringUtils.hasText(dateTo)) {
            throw new IllegalArgumentException("적어도 하나의 검색어가 필요합니다.");
        }

        if (StringUtils.hasText(dateFrom)) {
            ReservationDateChecker.validateDateFormat(dateFrom);
        }

        if (StringUtils.hasText(dateTo)) {
            ReservationDateChecker.validateDateFormat(dateTo);
        }

    }

}
