package uos.cineseoul.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumCheckValidator implements ConstraintValidator<PhoneNumCheck, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value==null || value.matches("\\d{10,11}"));
    }
}