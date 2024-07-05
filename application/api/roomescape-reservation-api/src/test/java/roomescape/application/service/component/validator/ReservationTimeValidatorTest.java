package roomescape.application.service.component.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.application.domain.reservationtime.service.component.validator.CreateReservationTimeValidator;
import roomescape.application.error.exception.CreateReservationTimeValidateException;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.reservationtime.vo.ReservationTimeStartAt;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static roomescape.application.error.code.ApplicationErrorCode.CANNOT_CREATE_EXIST_RESERVATION_TIME;

@ExtendWith(MockitoExtension.class)
class ReservationTimeValidatorTest {

    @InjectMocks
    private CreateReservationTimeValidator validator;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("모든 값이 같은 예약 시간은 생성할 수 없다.")
    void CannotCreateExistStartAt() {

        given(reservationTimeRepository.existByStartAt(any())).willReturn(true);

        CreateReservationTimeValidateException exception = assertThrows(
                CreateReservationTimeValidateException.class,
                () -> validator.validate(
                        new ReservationTime(
                                new ReservationTimeId(1L),
                                new ReservationTimeStartAt(LocalTime.of(12, 55))
                        )
                ));
        Assertions.assertThat(exception.getCode()).isEqualTo(CANNOT_CREATE_EXIST_RESERVATION_TIME);
    }
}
