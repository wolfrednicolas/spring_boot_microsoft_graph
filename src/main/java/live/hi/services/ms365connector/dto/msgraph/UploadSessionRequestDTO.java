package live.hi.services.ms365connector.dto.msgraph;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadSessionRequestDTO {
    @JsonProperty("AttachmentItem")
    private AttachmentItemDTO attachmentItem;

    public UploadSessionRequestDTO(AttachmentItemDTO attachmentItem) {
        this.attachmentItem = attachmentItem;
    }
    
}
