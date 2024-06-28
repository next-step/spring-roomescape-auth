package roomescape.globalfixture.dto;

import roomescape.reservation.dto.TimeDto;

public class TimeDtoFixture {

    public static TimeDto timeDtoCreate(){
        return new TimeDto(1L, "10:00");
    }
}
