package fortov.egor.diploma.user.dto;

import fortov.egor.diploma.validation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

// input validation takes place in dto
@Data
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank
    @Size(min = 2, max = 250, message = "Имя пользователя должно содержать от 2-х до 250-ти символов")
    private String name;

    @NotNull
    @Email
    @Size(min = 6, max = 100, message = "Почта пользователя должна содержать от 6 до 100 символов")
    private String email;

    @NotNull
    @Phone
    private String phone;

    @NotBlank
    @Size(min=8, max = 40, message = "Пароль должен иметь длину от 8 до 40 символов")
    private String password;
}
