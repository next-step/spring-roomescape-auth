package roomescape.support.checker;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReservationDateChecker {

    private ReservationDateChecker() {
        throw new UnsupportedOperationException("인스턴스화 할 수 없는 클래스입니다.");
    }

    public static void validateDate(String date) {
        if (!StringUtils.hasText(date)) {
            String message = String.format("필수 값은 비어 있을 수 없습니다. date = %s", date);
            throw new IllegalArgumentException(message);
        }

        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new IllegalArgumentException("Date must be in the format yyyy-MM-dd");
        }

        if (ReservationDateChecker.isAvailableDateTime(date)) {
            throw new IllegalArgumentException("이미 종료된 예약입니다");
        }
    }

    public static boolean isAvailableDateTime(String date) {
        LocalDate reservationTime = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return reservationTime.isBefore(LocalDate.now());
    }

}
