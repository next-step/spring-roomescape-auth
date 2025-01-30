package roomescape.support;

import roomescape.domain.common.ClockHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FakeClockHolder implements ClockHolder {

    private final LocalDateTime currentSeoulTime;

    public FakeClockHolder(final LocalDateTime currentSeoulTime) {
        this.currentSeoulTime = currentSeoulTime;
    }

    @Override
    public LocalDateTime getCurrentSeoulTime() {
        return this.currentSeoulTime;
    }

    @Override
    public ZonedDateTime getCurrentSeoulZonedDateTime() {
        return getCurrentSeoulTime().atZone(ZoneId.of("Asia/Seoul"));
    }
}
