package nextstep.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.reservation.ReservationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addReservation() throws Exception {
        ReservationRequest request = new ReservationRequest(
                "2022-08-11",
                "13:00",
                "name"
        );
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(post("/reservations")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void addReservationAlreadyExisted() throws Exception {
        addReservation();
        ReservationRequest request = new ReservationRequest(
                "2022-08-11",
                "13:00",
                "name"
        );
        String content = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(post("/reservations")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void showReservations() throws Exception {
        addReservation();
        mockMvc.perform(get("/reservations")
                        .param("date", "2022-08-11")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andDo(print());
    }

    @Test
    void deleteReservation() throws Exception {
        addReservation();
        mockMvc.perform(delete("/reservations")
                        .param("date", "2022-08-11")
                        .param("time", "13:00"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}