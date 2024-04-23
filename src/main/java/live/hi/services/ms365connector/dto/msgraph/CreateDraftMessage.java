package live.hi.services.ms365connector.dto.msgraph;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import live.hi.services.ms365connector.validation.ValidBody;
import live.hi.services.ms365connector.validation.ValidRecipients;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDraftMessage {
    @NotEmpty(message = "Subject cannot be empty")
    @NotNull(message = "Subject should not be null")
    private String subject;

    @ValidBody
    private Body body;

    @ValidRecipients(fieldName = "toRecipients")
    private List<Recipient> toRecipients;

    @ValidRecipients(fieldName = "ccRecipients", allowEmptyNode = true)
    private List<Recipient> ccRecipients = new ArrayList<>();
    
    @Nullable
    @Pattern(regexp = "^(low|normal|high)$", message = "Importance must be 'low', 'normal', or 'high'")
    private String importance;


    public CreateDraftMessage(String subject,
                   @JsonProperty("body") Body body,
                   @JsonProperty("toRecipients") List<Recipient> toRecipients,
                   @JsonProperty("ccRecipients") List<Recipient> ccRecipients,
                   @JsonProperty("importance") String importance){
        this.subject = subject;
        this.body = body;
        this.toRecipients = toRecipients;
        if (ccRecipients != null) {
            this.ccRecipients = ccRecipients;
        }
        this.importance = importance;
    }
    
}
