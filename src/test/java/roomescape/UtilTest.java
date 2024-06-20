package roomescape;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.reservation.error.exception.ReservationException;
import roomescape.domain.reservation.error.exception.ReservationErrorCode;
import roomescape.domain.time.error.exception.TimeErrorCode;
import roomescape.domain.time.error.exception.TimeException;
import roomescape.utils.FormatCheckUtil;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"24:01", "-1:32", "12;30", "01:61"})
    void 시간_형식이_맞지_않는_경우_예외를_발생시킨다(String startAt) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.startAtFormatCheck(startAt)).isInstanceOf(TimeException.class).hasMessage(TimeErrorCode.INVALID_TIME_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"00:00", "12:00", "23:59"})
    void 시간_형식이_맞는_경우_예외가_발생하지_않는다(String startAt) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.startAtFormatCheck(startAt)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"가", "a", "가나다라마바사아자차카타파하거너더러머버서"})
    void 예약_생성_중에_이름의_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongNameExample) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.reservationNameFormatCheck(wrongNameExample)
        ).isInstanceOf(ReservationException.class)
                .hasMessage(ReservationErrorCode.INVALID_RESERVATION_NAME_FORMAT_ERROR.getErrorMessage());

    }

    @ParameterizedTest
    @ValueSource(strings = {"박민욱", "브라운", "uzbeksAliddin Buriev"})
    void 예약_생성_중에_이름의_형식이_맞는_경우_예외를_발생하지_않는다(String rightNameExample) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.reservationNameFormatCheck(rightNameExample)
        ).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024.06.12", "2023-02-29", "2021-04-31", "2100-01-01", "1899-12-31"})
    void 예약_생성_중에_날짜의_형식이_맞지_않는_경우_예외를_발생시킨다(String wrongDateExample) {

        //when, then
        assertThatThrownBy(() -> FormatCheckUtil.reservationDateFormatCheck(wrongDateExample)
        ).isInstanceOf(ReservationException.class)
                .hasMessage(ReservationErrorCode.INVALID_RESERVATION_DATE_FORMAT_ERROR.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-06-12", "2024-02-29", "1999-12-31"})
    void 예약_생성_중에_날짜의_형식이_맞는_경우_예외를_발생하지_않는다(String rightDateExample) {

        //when, then
        assertThatCode(() -> FormatCheckUtil.reservationDateFormatCheck(rightDateExample)
        ).doesNotThrowAnyException();
    }
}
