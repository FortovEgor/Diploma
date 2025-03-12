package fortov.egor.diploma;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Notification {
    private Long id;
    private String type;

    @NotNull
    @Max(254)
    private String content;

    private LocalDateTime time_to_show;

    private Duration interval_to_repeat;

    @NotNull
    private Long userId;

    private Boolean immediately;
}
