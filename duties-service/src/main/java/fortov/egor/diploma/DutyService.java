package fortov.egor.diploma;

import fortov.egor.diploma.dto.CreateDutyRequest;
import fortov.egor.diploma.dto.DutyDto;
import fortov.egor.diploma.dto.UpdateDutyRequest;
import fortov.egor.diploma.dto.UserDutyDto;
import fortov.egor.diploma.storage.DutyStorage;
import fortov.egor.diploma.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DutyService {
    private final DutyStorage dutyStorage;
    private final DutyMapper mapper;

    public DutyDto getDuty(Long dutyId) {
        log.info("getting duty with id = {}", dutyId);
        Duty duty = dutyStorage.getDutyById(dutyId);
        if (duty == null) {
            throw new NotFoundException("Failed to find duty with id = " + dutyId);
        }

        return DutyDto.builder()
                .id(duty.getId())
                .start_time(duty.getStart_time())
                .interval(duty.getInterval())
                .ids(duty.getIds())
                .currentDutyUserId(getCurrentlyDutyUserId(duty))
                .build();
    }

    @Transactional
    public Duty createDuty(CreateDutyRequest request) {
        log.info("creating new duty: {}", request);
        Duty duty = mapper.toDuty(request);
        return dutyStorage.save(duty);
    }

    @Transactional
    public Duty updateDuty(UpdateDutyRequest request) {
        log.info("updating duty: {}", request);
        Long dutyId = request.getId();
        Duty duty = dutyStorage.getDutyById(dutyId);
        if (duty == null) {
            throw new NotFoundException("Failed to find duty with id = " + dutyId);
        }

        if (request.getStart_time() == null) {
            request.setStart_time(duty.getStart_time());
        }
        if (request.getInterval() == null) {
            request.setInterval(duty.getInterval());
        }
        if (request.getIds() == null) {
            request.setIds(duty.getIds());
        }

        Duty dutyResult = mapper.toDuty(request);
        return dutyStorage.update(dutyResult);
    }

    @Transactional
    public void deleteDuty(Long dutyId) {
        log.info("deleting duty with id = {}", dutyId);
        Duty duty = dutyStorage.getDutyById(dutyId);
        if (duty == null) {
            throw new NotFoundException("Failed to find duty with id = " + dutyId);
        }
        dutyStorage.delete(dutyId);
    }

    public UserDutyDto getNextUserDuty(Long userId) {
        log.info("getting next user duty, user id = {}", userId);
        List<Duty> userDuties= dutyStorage.getUserDuties(userId);
        if (userDuties.isEmpty()) {
            return null;
        }

        LocalDateTime nextUserDutyStart = null;
        Duty nextUserDuty = null;
        LocalDateTime now = LocalDateTime.now();

        for (Duty duty : userDuties) {
            Integer userIndexInIds = Arrays.stream(duty.getIds()).toList().indexOf(userId);
            LocalDateTime userFirstDuty = duty.getStart_time().plus(duty.getInterval().multipliedBy(userIndexInIds));

            if (userFirstDuty.equals(now) || userFirstDuty.isAfter(now)) {  // пользователь еще не дежурил
                                                                            // / первое дежурство наступает сейчас
                if (nextUserDutyStart == null || nextUserDutyStart.isAfter(userFirstDuty)) {
                    nextUserDutyStart = userFirstDuty;
                    nextUserDuty = duty;
                }
            } else {
                Duration dutyFullInterval = duty.getInterval().multipliedBy(duty.getIds().length);
                Duration diffBetweenUserFirstDutyAndNow = Duration.between(userFirstDuty, now);

                // Преобразование интервалов в наносекунды
                long dutyFullIntervalNanos = dutyFullInterval.toNanos();
                long diffBetweenUserFirstDutyAndNowNanos = diffBetweenUserFirstDutyAndNow.toNanos();

                long fullDutyCycles = -1;  // сколько полных циклов дежурств прошло
                if (diffBetweenUserFirstDutyAndNowNanos != 0) {
                    fullDutyCycles = dutyFullIntervalNanos / diffBetweenUserFirstDutyAndNowNanos;
                }

                if (fullDutyCycles == -1) {
                    log.error("Internal logic error");
                    return null;
                }

                LocalDateTime candidate = userFirstDuty.plus(dutyFullInterval.multipliedBy(fullDutyCycles));
                if (nextUserDutyStart == null || nextUserDutyStart.isAfter(candidate)) {
                    nextUserDutyStart = candidate;
                    nextUserDuty = duty;
                }
            }
        }

        return UserDutyDto
                .builder()
                .nextDutyDate(nextUserDutyStart)
                .duration(nextUserDuty.getInterval())
                .intervalBetweenDuties(nextUserDuty.getInterval().multipliedBy(nextUserDuty.getIds().length))
                .build();
    }

    private Long getCurrentlyDutyUserId(Duty duty) {
        LocalDateTime startTime = duty.getStart_time();
        Duration interval = duty.getInterval();
        LocalDateTime now = LocalDateTime.now();

        if (startTime.isAfter(now) || duty.getIds() == null || duty.getIds().length == 0){
            return null;
        }

        // duty has already started
        Duration dutyFullInterval = interval.multipliedBy(duty.getIds().length);
        Duration diffBetweenDutyStartAndNow = Duration.between(startTime, now);

        // Преобразование интервалов в наносекунды
        long dutyFullIntervalNanos = dutyFullInterval.toNanos();
        long diffBetweenDutyStartAndNowNanos = diffBetweenDutyStartAndNow.toNanos();
        long diffBetweenDutyStartAndNowNanosWithoutCycles = diffBetweenDutyStartAndNowNanos % dutyFullIntervalNanos;
        long userDutyInterval = dutyFullIntervalNanos / duty.getIds().length;

        return diffBetweenDutyStartAndNowNanosWithoutCycles / userDutyInterval;
    }
}
