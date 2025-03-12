package fortov.egor.diploma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;

@Data
public class UpdateNotificationRequest {
    private Long id;

    private String type;

    private String content;

    private String time_to_show;

    private Duration interval_to_repeat;

    private Long userId;

    private Boolean immediately;
}
