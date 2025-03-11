package fortov.egor.diploma.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class CreateDutyRequest {
    @NotNull(message = "Необходимо специфицировать время начала дежурства")
    LocalDateTime start_time;

    @NotNull(message = "Интервал необходим для определения времени работы одного дежурного")
    Duration interval;

    @NotNull(message = "Должен быть назначен список дежурных")
    Long[] ids;
}
