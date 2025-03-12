package fortov.egor.diploma.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
