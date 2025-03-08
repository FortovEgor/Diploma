package fortov.egor.diploma;

import fortov.egor.diploma.dto.CreateDutyRequest;
import fortov.egor.diploma.dto.UpdateDutyRequest;
import fortov.egor.diploma.dto.UserDutyDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/duties")
@RequiredArgsConstructor
public class DutyController {
    private final DutyService dutyService;

    @PostMapping
    public Duty createDuty(@Valid @RequestBody CreateDutyRequest request) {
        return dutyService.createDuty(request);
    }

    @PutMapping
    public Duty updateDuty(@Valid @RequestBody UpdateDutyRequest request) {
        return dutyService.updateDuty(request);
    }

    @GetMapping
    public UserDutyDto getUserDuty(@RequestParam Long userId) {
        return dutyService.getNextUserDuty(userId);
    }

    @DeleteMapping("/{dutyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Void put(@PathVariable Long dutyId) {
        dutyService.deleteDuty(dutyId);
        return null;
    }
}
