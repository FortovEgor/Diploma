package fortov.egor.diploma.user;

import fortov.egor.diploma.user.dto.NewUserRequest;
import fortov.egor.diploma.user.dto.UserDto;
import fortov.egor.diploma.user.dto.UserDtoPartial;
import fortov.egor.diploma.user.dto.UserFullInfoDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    User toUser(NewUserRequest request);

    UserDto toDto(User user);

    UserFullInfoDto toFullInfo(User user);

    List<UserDtoPartial> toPartial(List<User> users);
}
