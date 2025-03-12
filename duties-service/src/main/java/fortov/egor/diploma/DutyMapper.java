package fortov.egor.diploma;

import fortov.egor.diploma.dto.CreateDutyRequest;
import fortov.egor.diploma.dto.UpdateDutyRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DutyMapper {

    Duty toDuty(CreateDutyRequest request);
    Duty toDuty(UpdateDutyRequest request);
}
