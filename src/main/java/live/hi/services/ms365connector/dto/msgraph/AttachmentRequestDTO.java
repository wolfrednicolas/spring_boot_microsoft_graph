package live.hi.services.ms365connector.dto.msgraph;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentRequestDTO {
    @NotEmpty(message = "path cannot be empty")
    @NotNull(message = "path cannot be null")
    private String path;

    public AttachmentRequestDTO(@JsonProperty("path") String path) {
        this.path = path;
    }

    public AttachmentRequestDTO() {
    }
    
}
