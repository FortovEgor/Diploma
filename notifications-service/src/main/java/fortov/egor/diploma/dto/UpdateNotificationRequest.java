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

    private String timeToShow;  // todo: phone format validation\

    private Duration intervalToRepeat;

    private Long userId;

    private Boolean immediately;
}
