package fortov.egor.diploma.dto;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class UpdateNotificationRequest {
    private Long id;

    private String type;

    private String content;

    private LocalDateTime time_to_show;

    private Duration interval_to_repeat;

    private Long userId;

    private Boolean immediately;
}
