package fortov.egor.diploma.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";

    @Override
    public void initialize(Phone constraintAnnotation) { }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            return false;
        }
        return Pattern.matches(PHONE_PATTERN, phone);
    }
}