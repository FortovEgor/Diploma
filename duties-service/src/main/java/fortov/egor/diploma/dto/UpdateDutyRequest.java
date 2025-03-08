package fortov.egor.diploma.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateDutyRequest {
    LocalDateTime start_time;

    Long interval;

    Long[] ids;
}
