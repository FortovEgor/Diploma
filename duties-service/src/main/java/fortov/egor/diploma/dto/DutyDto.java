package fortov.egor.diploma.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class DutyDto {
    Long id;

    LocalDateTime start_time;

    Duration interval;

    Long[] ids;

    Long currentDutyUserId;
}
