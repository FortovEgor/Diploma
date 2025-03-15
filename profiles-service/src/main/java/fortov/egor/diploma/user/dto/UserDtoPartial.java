package fortov.egor.diploma.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoPartial {
    private Long id;

    private String name;

    private String avatar;
}
