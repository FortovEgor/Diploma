package fortov.egor.diploma.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;

@Data
public class CreateNotificationRequest {

    @NotNull
    private String type;

    @NotNull
    private String content;

    private String time_to_show;  // todo: phone format validation\

    private Duration interval_to_repeat;

    @NotBlank
    private Long userId;

    private Boolean immediately;
}
