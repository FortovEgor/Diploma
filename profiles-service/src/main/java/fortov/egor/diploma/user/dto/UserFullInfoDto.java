package fortov.egor.diploma.user.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class UserFullInfoDto {
    private Long id;

    private String name;

    private String email;

    private String organization;

    private Integer avatar;

    private String about;
}
