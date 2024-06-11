package roomescape.application.dto;

import roomescape.annotation.TimeCheck;

public record ReservationTimeCommand(@TimeCheck String startAt) {

}
