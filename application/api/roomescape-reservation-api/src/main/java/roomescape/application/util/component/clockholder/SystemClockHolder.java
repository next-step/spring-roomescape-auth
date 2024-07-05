package roomescape.application.util.component.clockholder;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
