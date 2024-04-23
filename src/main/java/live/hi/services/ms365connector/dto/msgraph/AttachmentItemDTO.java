package live.hi.services.ms365connector.dto.msgraph;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentItemDTO {

    private String attachmentType;
    private String name;
    private long size;

    public AttachmentItemDTO( String attachmentType,
        String name,
        long size
    ) {
        this.attachmentType = attachmentType;
        this.name = name;
        this.size = size;
    }

    public AttachmentItemDTO(){

    }
}
