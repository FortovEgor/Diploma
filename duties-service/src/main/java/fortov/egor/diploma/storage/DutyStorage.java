package fortov.egor.diploma.storage;

import fortov.egor.diploma.Duty;
import fortov.egor.diploma.dto.UserDutyDto;

import java.util.List;

public interface DutyStorage {
    // todo: implement
    Duty save(Duty duty);

    Duty update(Duty duty);

    void delete(Long dutyId);

    List<Duty> getUserDuties(Long userId);

    List<Duty> getDutiesByIds(List<Long> dutiesIds);

    Duty getDutyById(Long dutyId);
}
