package roomescape.domain.common;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface ClockHolder {

    LocalDateTime getCurrentSeoulTime();

    ZonedDateTime getCurrentSeoulZonedDateTime();
}
