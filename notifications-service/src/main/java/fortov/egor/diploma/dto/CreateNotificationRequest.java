package fortov.egor.diploma.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor
public class CreateNotificationRequest {

    @NotNull(message = "Уведомление должно иметь тип")
    private String type;

    @NotNull(message = "Уведомление должно содержать контент")
    private String content;

    private String time_to_show;

    private Duration interval_to_repeat;

    @NotNull(message = "Уведомление должно быть назначено кому-либо")
    private Long userId;

    private Boolean immediately;
}
