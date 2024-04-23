package live.hi.services.ms365connector.dto.msgraph;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAddress {
    private String address;

    public EmailAddress(@JsonProperty("address") String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "EmailAddress{" +
                "address='" + address + '\'' +
                '}';
    }
}
