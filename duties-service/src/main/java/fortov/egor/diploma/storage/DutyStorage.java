package fortov.egor.diploma.storage;

import fortov.egor.diploma.Duty;

import java.util.List;

public interface DutyStorage {
    Duty save(Duty duty);

    Duty update(Duty duty);

    void delete(Long dutyId);

    List<Duty> getUserDuties(Long userId);

    List<Duty> getDutiesByIds(List<Long> dutiesIds);

    Duty getDutyById(Long dutyId);

    List<Duty> getAllDuties();
}
