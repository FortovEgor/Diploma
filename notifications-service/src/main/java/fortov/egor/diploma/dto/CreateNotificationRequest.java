package fortov.egor.diploma.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
//@DynamicInsert // ignore null fields
@AllArgsConstructor
public class CreateNotificationRequest {

    @NotNull(message = "Уведомление должно иметь тип")
    @NotificationType
    private String type;

    @NotNull(message = "Уведомление должно содержать контент")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time_to_show;

    private Duration interval_to_repeat;

    @NotNull(message = "Уведомление должно быть назначено кому-либо")
    private Long userId;

    private Boolean immediately;
}
