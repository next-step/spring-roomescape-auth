package roomescape.reservationtime.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.reservationtime.domain.ReservationTimeRepository;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Service
public class ReservationTimeValidator {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeValidator(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public void checkPresent(long id) {
        reservationTimeRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("존재하지 않는 예약 시간입니다."));
    }

    public void validateRequest(ReservationTimeRequest reservationTimeRequest) {
        String startAt = reservationTimeRequest.startAt();

        checkValidTime(startAt);
        checkDuplicated(startAt);
    }

    private void checkValidTime(String startAt) {
        try {
            LocalTime.parse(startAt);
        }
        catch (DateTimeParseException exception) {
            throw BadRequestException.of("유효하지 않은 시간 형식입니다.");
        }
    }

    private void checkDuplicated(String startAt) {
        boolean isDuplicated = reservationTimeRepository.findByStartAt(startAt).isPresent();

        if (isDuplicated) {
            throw BadRequestException.of("이미 존재하는 시간입니다.");
        }
    }
}
