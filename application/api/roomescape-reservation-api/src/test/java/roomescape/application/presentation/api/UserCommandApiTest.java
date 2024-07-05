package roomescape.application.presentation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.application.domain.user.api.UserCommandApi;
import roomescape.application.domain.user.api.dto.request.CreateUserRequest;
import roomescape.application.domain.user.service.UserService;
import roomescape.domain.user.User;
import roomescape.domain.user.vo.UserEmail;
import roomescape.domain.user.vo.UserId;
import roomescape.domain.user.vo.UserName;
import roomescape.domain.user.vo.UserPassword;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserCommandApi.class)
class UserCommandApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                new UserId(1L),
                new UserName("kilian"),
                new UserEmail("kilian@gmail.com"),
                new UserPassword("1234")
        );
    }

    @Test
    @DisplayName("사용자 생성 API 컨트롤러 테스트")
    void loginTest() throws Exception {
        CreateUserRequest request = new CreateUserRequest("kilian@gmail.com", "1234", "kilian");

        given(userService.create(any())).willReturn(user);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());
    }
}
