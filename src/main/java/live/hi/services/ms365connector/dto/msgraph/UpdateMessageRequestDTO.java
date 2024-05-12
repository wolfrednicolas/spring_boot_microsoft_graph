package live.hi.services.ms365connector.dto.msgraph;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMessageRequestDTO {
    @JsonProperty("isRead")
    private String isRead;
}
