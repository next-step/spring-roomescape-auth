package roomescape.utils;

import roomescape.domain.time.error.exception.TimeErrorCode;
import roomescape.domain.time.error.exception.TimeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeCheckUtil {

    public static void isBeforeCheck(String date, String startAt) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime inputDateTime = LocalDateTime.parse(date + " " + startAt, dateTimeFormatter);
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (inputDateTime.isBefore(currentDateTime)) {
            throw new TimeException(TimeErrorCode.IS_BEFORE_ERROR);
        }
    }
}
