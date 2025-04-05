package fortov.egor.diploma.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateDutyRequest {
    @NotNull
    Long id;

    String name;

    LocalDateTime start_time;

    Duration interval;

    Long[] ids;
}
