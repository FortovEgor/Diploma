package fortov.egor.diploma.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoPartial {
    private Long id;

    private String name;

    private Integer avatar;
}
