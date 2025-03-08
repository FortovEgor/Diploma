package fortov.egor.diploma.dto;

import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
public class UserDutyDto {
    LocalDateTime nextDutyDate;
    Duration duration;
    Duration intervalBetweenDuties;
}
