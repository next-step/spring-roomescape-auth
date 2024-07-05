package roomescape.application.presentation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.application.domain.reservation.api.ReservationCommandApi;
import roomescape.application.domain.reservation.api.dto.request.CreateReservationRequest;
import roomescape.application.domain.reservation.api.dto.response.CreateReservationResponse;
import roomescape.application.domain.reservation.service.ReservationCommandService;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.vo.ReservationDate;
import roomescape.domain.reservation.vo.ReservationId;
import roomescape.domain.reservation.vo.ReservationName;
import roomescape.domain.reservationtime.vo.ReservationTimeId;
import roomescape.domain.theme.vo.ThemeId;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationCommandApi.class)
class ReservationCommandApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockBean
    private ReservationCommandService reservationCommandService;

    @Autowired
    private MockMvc mockMvc;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = Reservation.of(
                new ReservationId(1L),
                new ReservationName("kilian"),
                new ReservationDate(LocalDate.of(2024, 7, 3)),
                new ReservationTimeId(1L),
                new ThemeId(1L)
        );
    }

    @Test
    @DisplayName("예약 생성 API 컨트롤러 테스트")
    void createReservationTest() throws Exception {
        String reservationName = "kilian";
        CreateReservationRequest request =
                new CreateReservationRequest(reservationName, "2024-07-03", 1L, 1L);

        given(reservationCommandService.create(any()))
                .willReturn(reservation);

        CreateReservationResponse response = CreateReservationResponse.from(reservation);

        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("예약 삭제 API 컨트롤러 테스트")
    void deleteReservationTest() throws Exception {
        Long reservationId = 1L;

        doNothing().when(reservationCommandService).delete(any());

        mockMvc.perform(
                        delete("/reservations/{reservationId}", reservationId)
                )
                .andExpect(status().isNoContent());
    }
}
