package uos.cineseoul.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdStartsWithEnglishValidator.class)
public @interface IdStartsWithEnglish {
    String message() default "id must start with an English letter";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
