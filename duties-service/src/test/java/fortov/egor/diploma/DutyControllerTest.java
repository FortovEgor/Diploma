package fortov.egor.diploma;

import com.fasterxml.jackson.databind.ObjectMapper;
import fortov.egor.diploma.dto.CreateDutyRequest;
import fortov.egor.diploma.dto.DutyDto;
import fortov.egor.diploma.dto.UpdateDutyRequest;
import fortov.egor.diploma.dto.UserDutyDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
                "duty name",
                LocalDateTime.now(),
                Duration.ofMinutes(5),
                ids);
    }

    @Nested
    class CreateDuty {
        @Test
        void createDutySuccessful() throws Exception {
            CreateDutyRequest request = new CreateDutyRequest(
                    duty.getStart_time(),
                    duty.getName(),
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
                    .andExpect(jsonPath("$.start_time",
                            equalTo(duty.getStart_time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                    .andExpect(jsonPath("$.interval", equalTo(duty.getInterval().toString())))
                    .andExpect(jsonPath("$.ids", containsInAnyOrder(1, 2, 3)));
        }
    }

    @Nested
    class UpdateDuty {
        @Test
        void updateDuty() throws Exception {
            UpdateDutyRequest request = new UpdateDutyRequest(
                    1L,
                    "New name",
                    duty.getStart_time(),
                    duty.getInterval(),
                    duty.getIds()
            );

            Mockito
                    .when(dutyService.updateDuty(request))
                    .thenReturn(duty);

            mvc.perform(put("/duties")
                            .content(mapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(duty.getId()), Long.class))
                    .andExpect(jsonPath("$.start_time",
                            equalTo(duty.getStart_time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                    .andExpect(jsonPath("$.interval", equalTo(duty.getInterval().toString())))
                    .andExpect(jsonPath("$.ids", containsInAnyOrder(1, 2, 3)));
        }
    }

    @Nested
    class GetDuty {
        @Test
        void getDutySuccessful() throws Exception {
            Mockito
                    .when(dutyService.getDuty(duty.getId()))
                    .thenReturn(DutyDto.builder()
                            .id(duty.getId())
                            .start_time(duty.getStart_time())
                            .interval(duty.getInterval())
                            .ids(duty.getIds())
                            .currentDutyUserId(1L)
                            .build());

            mvc.perform(get("/duties/" + duty.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.id", equalTo(duty.getId()), Long.class))
                    .andExpect(jsonPath("$.start_time",
                            equalTo(duty.getStart_time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                    .andExpect(jsonPath("$.interval", equalTo(duty.getInterval().toString())));
        }

        @Test
        void getAllDutiesSuccessful() throws Exception {
            Mockito
                    .when(dutyService.getAllDuties())
                    .thenReturn(Arrays.asList(DutyDto.builder()
                            .id(duty.getId())
                            .start_time(duty.getStart_time())
                            .interval(duty.getInterval())
                            .ids(duty.getIds())
                            .currentDutyUserId(1L)
                            .build()));

            mvc.perform(get("/duties")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$[0].id", equalTo(duty.getId()), Long.class))
                    .andExpect(jsonPath("$[0].start_time",
                            equalTo(duty.getStart_time().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                    .andExpect(jsonPath("$[0].interval", equalTo(duty.getInterval().toString())));
        }

        @Test
        void getNextUserDutySuccessful() throws Exception {
            Mockito
                    .when(dutyService.getNextUserDuty(anyLong()))
                    .thenReturn(UserDutyDto
                            .builder()
                            .nextDutyDate(LocalDateTime.now())
                            .duration(Duration.ofMinutes(11))
                            .intervalBetweenDuties(Duration.ofMinutes(11))
                            .build());

            mvc.perform(get("/duties/user/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is2xxSuccessful());
        }
    }

    // for future releases ONLY:
//    @Nested
//    class DeleteDuty {
//        @Test
//        void deleteDuty() throws Exception{
//            doNothing().when(dutyService).deleteDuty(anyLong());
//
//            mvc.perform(delete("/duties/" + duty.getId())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().is2xxSuccessful());
//        }
//    }
}