package fortov.egor.diploma.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor
public class CreateNotificationRequest {

    @NotNull
    private String type;

    @NotNull
    private String content;

    private String time_to_show;

    private Duration interval_to_repeat;

    @NotNull
    private Long userId;

    private Boolean immediately;
}
