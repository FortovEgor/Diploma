package fortov.egor.diploma.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFullInfoDto {
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String organization;

    private String avatar;

    private String about;
}
