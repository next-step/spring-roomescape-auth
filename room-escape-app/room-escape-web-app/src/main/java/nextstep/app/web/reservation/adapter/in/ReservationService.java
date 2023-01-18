package nextstep.app.web.reservation.adapter.in;

import nextstep.app.web.schedule.adapter.in.ScheduleService;
import nextstep.core.reservation.Reservation;
import nextstep.core.reservation.in.ReservationCreateRequest;
import nextstep.core.reservation.in.ReservationResponse;
import nextstep.core.reservation.in.ReservationUseCase;
import nextstep.core.reservation.out.ReservationRepository;
import nextstep.core.schedule.in.ScheduleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService implements ReservationUseCase {
    private final ReservationRepository repository;
    private final ScheduleService scheduleService;

    public ReservationService(ReservationRepository repository, ScheduleService scheduleService) {
        this.repository = repository;
        this.scheduleService = scheduleService;
    }

    @Transactional
    public ReservationResponse create(ReservationCreateRequest request, Long memberId) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(memberId);

        validateExistsSchedule(request.scheduleId());
        validateDuplicatedScheduleIdAndDateAndTime(request);

        Reservation reservation = request.to(memberId);

        Reservation saved = repository.save(reservation);
        return ReservationResponse.from(saved);
    }

    private void validateExistsSchedule(Long scheduleId) {
        if (!scheduleService.exists(scheduleId)) {
            throw new IllegalArgumentException("스케줄이 존재하지 않습니다.");
        }
    }

    private void validateDuplicatedScheduleIdAndDateAndTime(ReservationCreateRequest request) {
        if (repository.existsById(request.scheduleId())) {
            throw new IllegalArgumentException("동일한 날짜와 시간엔 예약할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findReservations(Long themeId, LocalDate date) {
        Objects.requireNonNull(themeId);
        Objects.requireNonNull(date);

        ScheduleResponse schedule = scheduleService.findByThemeIdAndDate(themeId, date);
        return repository.findAllByScheduleIdAndDate(schedule.getId(), date)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long reservationId, Long memberId) {
        Objects.requireNonNull(reservationId);
        validateExistsReservation(reservationId);

        repository.deleteById(reservationId);
    }

    private void validateExistsReservation(Long reservationId) {
        if (!repository.existsById(reservationId)) {
            throw new IllegalArgumentException("해당 날짜와 시간에 예약이 존재하지 않습니다.");
        }
    }
}
