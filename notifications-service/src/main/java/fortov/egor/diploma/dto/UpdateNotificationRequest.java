package fortov.egor.diploma.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
//@DynamicInsert // ignore null fields
@AllArgsConstructor
public class UpdateNotificationRequest {
    @NotNull
    private Long id;

    @NotificationType  // надо ???
    private String type;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time_to_show;

    private Duration interval_to_repeat;

    private Long userId;

    private Boolean immediately;
}
