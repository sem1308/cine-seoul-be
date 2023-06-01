package uos.cineseoul.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdStartsWithEnglishValidator implements ConstraintValidator<IdStartsWithEnglish, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("^[A-Za-z].*");
    }
}