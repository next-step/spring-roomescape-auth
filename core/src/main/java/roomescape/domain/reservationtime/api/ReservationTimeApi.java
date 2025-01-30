package roomescape.domain.reservationtime.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservationtime.application.ReservationTimeAppendRequest;
import roomescape.domain.reservationtime.application.ReservationTimeCommandService;
import roomescape.domain.reservationtime.application.ReservationTimeQueryService;
import roomescape.domain.reservationtime.application.response.AvailableReservationTimeResponse;
import roomescape.domain.reservationtime.domain.ReservationTime;
import roomescape.domain.reservationtime.domain.ReservationTimeId;
import roomescape.global.rest.ApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationTimeApi {

    private final ReservationTimeCommandService timeCommandService;
    private final ReservationTimeQueryService timeQueryService;

    @PostMapping("/api/times")
    public ApiResponse<ReservationTimeAppendHttpResponse> append(
            @RequestBody ReservationTimeAppendRequest request
    ) {
        final ReservationTime appended = timeCommandService.append(request);
        return ApiResponse.ok(ReservationTimeAppendHttpResponse.from(appended));
    }

    @GetMapping("/api/times")
    public ApiResponse<List<ReservationTimeAppendHttpResponse>> fetchAll() {
        List<ReservationTime> times = timeQueryService.fetchAll();
        return ApiResponse.ok(ReservationTimeAppendHttpResponse.from(times));
    }

    @DeleteMapping("/api/times/{timeId}")
    public ApiResponse<Object> delete(
            @PathVariable(name = "timeId") Long timeId
    ) {
        timeCommandService.delete(new ReservationTimeId(timeId));
        return ApiResponse.okWithEmptyData();
    }

    @GetMapping("/api/times/available")
    public ApiResponse<List<AvailableReservationTimeResponse>> getAvailableReservationTimes(
            @RequestParam LocalDate date,
            @RequestParam Long themeId
    ) {
        List<AvailableReservationTimeResponse> availableTimes =
                timeCommandService.getAvailableReservationTimes(date, themeId);

        return ApiResponse.ok(availableTimes);
    }
}
