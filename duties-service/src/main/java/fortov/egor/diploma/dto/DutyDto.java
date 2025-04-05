package fortov.egor.diploma.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class DutyDto {
    Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime start_time;

    Duration interval;

    Long[] ids;

    Long currentDutyUserId;
}
