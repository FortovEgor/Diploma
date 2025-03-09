package fortov.egor.diploma.user.dto;

import lombok.Getter;

@Getter
public class UserFullInfoDto {
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String organization;

    private Integer avatar;

    private String about;
}
