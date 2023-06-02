package uos.cineseoul.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegidentNumCheckValidator implements ConstraintValidator<ResidentNumCheck, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value==null || value.matches("\\d{13}"));
    }
}