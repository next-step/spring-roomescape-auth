package roomescape.fixture;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFixture {

    public static String formatCurrentDate(String format) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }
}
