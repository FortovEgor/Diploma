package fortov.egor.diploma;

import com.fasterxml.jackson.databind.ObjectMapper;
import fortov.egor.diploma.user.User;
import fortov.egor.diploma.user.UserController;
import fortov.egor.diploma.user.UserMapperImpl;
import fortov.egor.diploma.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import({DutyMapperImpl.class})
@WebMvcTest(controllers = DutyController.class)
class DutyControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DutyService dutyService;

    Duty duty;

    @BeforeEach
    void setup() {
        Long[] ids = {1L, 2L, 3L};
        duty = new Duty(1L,
                LocalDateTime.parse("2023-10-15T12:00:00"),
                Duration.parse("PT4H30M"), ids
        );
    }

    @Nested
    class CreateDuty {

    }


    @Test
    void createDuty() {
    }

    @Test
    void updateDuty() {
    }

    @Test
    void getDuty() {
    }

    @Test
    void getNextUserDuty() {
    }

    @Test
    void deleteDuty() {
    }
}