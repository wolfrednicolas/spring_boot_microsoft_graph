package live.hi.services.ms365connector.dto.msgraph;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipient {
    private EmailAddress emailAddress;

    public Recipient(@JsonProperty("emailAddress") EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "emailAddress=" + emailAddress +
                '}';
    }
}
