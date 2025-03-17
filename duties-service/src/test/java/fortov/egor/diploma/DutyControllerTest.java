package fortov.egor.diploma;

import com.fasterxml.jackson.databind.ObjectMapper;
import fortov.egor.diploma.dto.CreateDutyRequest;
import fortov.egor.diploma.user.dto.NewUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Import({DutyMapperImpl.class})
@WebMvcTest(controllers = DutyController.class)
@ContextConfiguration(classes = {DutyController.class})
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
                Duration.parse("PT4H30M"),
                ids);
    }

    @Nested
    class CreateDuty {
        @Test
        void createDutySuccessful() throws Exception {
            CreateDutyRequest request = new CreateDutyRequest(
                    duty.getStart_time(),
                    duty.getInterval(),
                    duty.getIds()
            );

            Mockito
                    .when(dutyService.createDuty(request))
                    .thenReturn(duty);

            mvc.perform(post("/duties")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", equalTo(duty.getId()), Long.class))
                    .andExpect(jsonPath("$.start_time", equalTo(duty.getStart_time())))
                    .andExpect(jsonPath("$.interval", equalTo(duty.getInterval())))
                    .andExpect(jsonPath("$.ids", equalTo(duty.getIds())));
        }
    }

    @Nested
    class UpdateDuty {
        @Test
        void updateDuty() {
            // @TODO: impl
        }
    }

    @Nested
    class GetDuty {
        @Test
        void getDuty() {
            // @TODO: impl
        }

        @Test
        void getNextUserDuty() {
            // @TODO: impl
        }
    }

    @Nested
    class DeleteDuty {
        @Test
        void deleteDuty() throws Exception{
            CreateDutyRequest request = new CreateDutyRequest(
                    duty.getStart_time(),
                    duty.getInterval(),
                    duty.getIds()
            );

            doNothing().when(dutyService).deleteDuty(anyLong());

            mvc.perform(delete("/duties/" + duty.getId())
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful());
        }
    }
}