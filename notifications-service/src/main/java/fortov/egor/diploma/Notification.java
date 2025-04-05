package fortov.egor.diploma;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Notification {
    private Long id;
    private String type;

    @NotNull
    @Max(254)
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time_to_show;

    private Duration interval_to_repeat;

    @NotNull
    private Long userId;

    private Boolean immediately;
}
