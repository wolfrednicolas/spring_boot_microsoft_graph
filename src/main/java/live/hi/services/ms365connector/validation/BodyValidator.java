package live.hi.services.ms365connector.validation;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import live.hi.services.ms365connector.dto.msgraph.Body;

public class BodyValidator implements ConstraintValidator<ValidBody, Body> {

    @Override
    public void initialize(ValidBody constraintAnnotation) {
    }

    @Override
    public boolean isValid(Body body, ConstraintValidatorContext context) {
        List<String> errors = new ArrayList<>();
        boolean check = true;
        if (body == null) {
            errors.add("Body cannot be null");
            check = false;
        } else {
            // Verify that contentType and content are present and have value
            if(body.getContentType() == null){
                errors.add("Body content type cannot be null");
                check = false;
            }else if(body.getContentType().isEmpty()){
                errors.add("Body content type cannot be empty");
                check = false;
            }
            if(body.getContent() == null){
                errors.add("Body content cannot be null");
            }else if(body.getContent().isEmpty()){
                errors.add("Body content cannot be empty");
            }

            if (check) {
                // Verify that the contentType is "HTML" or "text"
                if (!isValidContentType(body.getContentType())) {
                    errors.add("ContentType must be 'text' or 'HTML'");
                }
            }
        }

        if (!errors.isEmpty()) {
            buildConstraintViolation(context, errors);
            return false;
        }
        return true; // Validation is successful
    }

    // Helper method to validate the contentType
    private boolean isValidContentType(String contentType) {
        return contentType != null && (contentType.equals("HTML") || contentType.equals("text"));
    }

    // Helper method to construct the constraint violation with the error messages
    private void buildConstraintViolation(ConstraintValidatorContext context, List<String> errors) {
        context.disableDefaultConstraintViolation();
        for (String error : errors) {
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        }
    }
}
