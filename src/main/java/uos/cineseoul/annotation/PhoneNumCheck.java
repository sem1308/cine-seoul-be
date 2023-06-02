package uos.cineseoul.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumCheckValidator.class)
public @interface PhoneNumCheck {
    String message() default "Must be 10 or 11 digits long.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
