package fortov.egor.diploma.user.dto;

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

    private Integer avatar;

    private String about;
}
