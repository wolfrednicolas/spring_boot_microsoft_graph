package live.hi.services.ms365connector.dto.msgraph;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CopyDraftMessage {
    private String destinationId;

    public CopyDraftMessage(@JsonProperty("destinationId") String destinationId) {
        this.destinationId = destinationId;
    }

    public CopyDraftMessage() {
    }
}
