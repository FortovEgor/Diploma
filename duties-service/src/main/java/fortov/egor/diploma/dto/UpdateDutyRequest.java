package fortov.egor.diploma.dto;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class UpdateDutyRequest {
    LocalDateTime start_time;

    Duration interval;

    Long[] ids;
}
