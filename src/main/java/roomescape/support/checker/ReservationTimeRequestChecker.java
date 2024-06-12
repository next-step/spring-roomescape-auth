package roomescape.support.checker;

import org.springframework.util.StringUtils;

public class ReservationTimeRequestChecker {

    private ReservationTimeRequestChecker() {
        throw new UnsupportedOperationException("인스턴스화 할 수 없는 클래스입니다.");
    }

    public static void validateRequestParam(String date, String themeId) {
        if (!StringUtils.hasText(date) || !StringUtils.hasText(themeId)) {
            String message = String.format("필수 값은 비어 있을 수 없습니다. date: %s, themeId: %s", date, themeId);
            throw new IllegalArgumentException(message);
        }

        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new IllegalArgumentException("Date must be in the format yyyy-MM-dd");
        }

        if (ReservationDateChecker.isAvailableDateTime(date)) {
            throw new IllegalArgumentException("이미 종료된 예약입니다");
        }

        try {
            Long.parseLong(themeId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("themeId는 숫자이어야 합니다.");
        }

    }
}
