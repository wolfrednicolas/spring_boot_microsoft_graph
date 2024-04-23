package live.hi.services.ms365connector.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = BodyValidator.class)
@Documented
public @interface ValidBody {
    String message() default "Body should contain valid contentType and content";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}