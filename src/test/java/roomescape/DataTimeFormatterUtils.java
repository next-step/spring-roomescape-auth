package roomescape;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DataTimeFormatterUtils {

	private DataTimeFormatterUtils() {

	}

	public static String getFormattedTomorrowDate() {
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return tomorrow.format(formatter);
	}

}
