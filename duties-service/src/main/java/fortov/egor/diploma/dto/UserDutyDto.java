package fortov.egor.diploma.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class UserDutyDto {
    LocalDateTime nextDutyDate;
    Duration duration;
    Duration intervalBetweenDuties;
}
