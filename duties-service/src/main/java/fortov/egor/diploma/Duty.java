package fortov.egor.diploma;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Duty {
    private Long id;

    private String name;

    private LocalDateTime start_time;

    private Duration interval;

    private Long[] ids;
}
