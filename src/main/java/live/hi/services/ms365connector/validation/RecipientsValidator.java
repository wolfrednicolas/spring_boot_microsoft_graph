package live.hi.services.ms365connector.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import live.hi.services.ms365connector.dto.msgraph.Recipient;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class RecipientsValidator implements ConstraintValidator<ValidRecipients, Object> {

    private String fieldName; // Argument to identify the field name
    private boolean allowEmptyNode; // Argument to allow empty node

    @Override
    public void initialize(ValidRecipients constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName(); // Get the field name
        this.allowEmptyNode = constraintAnnotation.allowEmptyNode(); // Get the allow empty node
    }

    @Override
    public boolean isValid(Object input, ConstraintValidatorContext context) {
        if (input == null) {
            buildConstraintViolation(context, fieldName + " cannot be null");
            return false;
        }

        // Convert the input object to a list of recipients
        List<Recipient> toRecipients = (List<Recipient>) input;
        System.out.println("valor: "+allowEmptyNode);
        if (toRecipients.isEmpty() && !allowEmptyNode) {
            buildConstraintViolation(context, fieldName + " should not be empty");
            return false;
        }

        // Check if the recipient list is empty or if any of the recipients are invalid
        List<String> errors = new ArrayList<>();
        if (!isValidRecipients(toRecipients, errors)) {
            for (String error : errors) {
                buildConstraintViolation(context, error);
            }
            return false;
        }

        return true; //If all recipients are valid, validation is successful
    }

    // Helper method to check if all recipients are valid
    private boolean isValidRecipients(List<Recipient> recipients, List<String> errors) {
        //Regular expression to validate email address
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        for (int i = 0; i < recipients.size(); i++) {
            Recipient recipient = recipients.get(i);
            if (recipient.getEmailAddress() == null) {
                errors.add(fieldName + "[" + i + "].emailAddress cannot be null");
            } else if (recipient.getEmailAddress().getAddress() == null || recipient.getEmailAddress().getAddress().isEmpty()) {
                errors.add(fieldName + "[" + i + "].emailAddress should has address node");
            } else if (!pattern.matcher(recipient.getEmailAddress().getAddress()).matches()) {
                errors.add(fieldName + "[" + i + "].emailAddress.address invalid email format");
            }
        }

        return errors.isEmpty(); // If there are no errors, return true
    }

    // Helper method to construct the constraint violation with the error message
    private void buildConstraintViolation(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
               .addConstraintViolation();
    }
}
