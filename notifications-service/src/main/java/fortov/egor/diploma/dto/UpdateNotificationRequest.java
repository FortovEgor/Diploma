package fortov.egor.diploma.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateNotificationRequest {
    private Long id;

    private String type;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time_to_show;

    private Duration interval_to_repeat;

    private Long userId;

    private Boolean immediately;
}
