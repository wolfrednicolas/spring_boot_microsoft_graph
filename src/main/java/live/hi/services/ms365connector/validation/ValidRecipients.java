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
@Constraint(validatedBy = RecipientsValidator.class)
@Documented
public @interface ValidRecipients {
    String message() default "Recipients should not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String fieldName(); // Argument to identify the field name
    boolean allowEmptyNode() default false; // Argument to allow empty node
}