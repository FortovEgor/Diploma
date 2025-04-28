package fortov.egor.diploma.dto;

import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidatorContext;

public class NotificationTypeValidator implements ConstraintValidator<NotificationType, String> {

    private static final String[] ALLOWED_VALUES = {"sms", "email"};

    @Override
    public void initialize(NotificationType constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        for (String allowedValue : ALLOWED_VALUES) {
            if (value.equalsIgnoreCase(allowedValue)) {
                return true;
            }
        }
        return false;
    }
}
