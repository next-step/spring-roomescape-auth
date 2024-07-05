package roomescape.application.domain.reservationtime.api.dto.response;

import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimes;

import java.util.List;
import java.util.stream.Collectors;

public class FindAvailableTimesResponse {

    private static final String TIME_FORMAT = "HH:mm";

    private final Long id;

    private final String startAt;

    public FindAvailableTimesResponse(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static FindAvailableTimesResponse from(ReservationTime time) {
        return new FindAvailableTimesResponse(time.getId().id(), time.getFormatStartAt(TIME_FORMAT));
    }

    public static List<FindAvailableTimesResponse> from(ReservationTimes times) {
        return times.getReservationTimes().stream()
                .map(FindAvailableTimesResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
