package fortov.egor.diploma.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Объект не найден", e.getMessage());

        log.error(e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(NotValidInputParamException.class)
    public ResponseEntity<?> notValidInputParamException(NotValidInputParamException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Некорректное значение", e.getMessage());

        log.error(e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> conflictException(ConflictException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Конфликт", e.getMessage());

        log.error(e.getStackTrace()[0].getMethodName() + ": " + e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Обработка всех остальных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", "Internal exception occured");  //  for DEBUG use `e.getMessage()` instead

        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}