package roomescape.domain.reservationtime.vo;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record ReservationTimeStartAt(LocalTime startAt) {

    public String getFormat(String pattern) {
        return startAt.format(DateTimeFormatter.ofPattern(pattern));
    }
}
