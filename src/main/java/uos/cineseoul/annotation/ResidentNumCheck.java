package uos.cineseoul.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegidentNumCheckValidator.class)
public @interface ResidentNumCheck {
    String message() default "Must be 13 digits long.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
