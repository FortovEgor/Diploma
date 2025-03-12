package fortov.egor.diploma;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class Duty {
    private Long id;

    private LocalDateTime start_time;

    private Duration interval;

    private Long[] ids;
}
