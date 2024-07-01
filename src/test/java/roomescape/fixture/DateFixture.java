package roomescape.fixture;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFixture {

    public static String formatDate(String format, int amount) {
        LocalDate date = LocalDate.now();
        date = date.plusDays(amount);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }
}
