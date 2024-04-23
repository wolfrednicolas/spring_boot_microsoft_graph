package live.hi.services.ms365connector.dto.msgraph;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotReadMessageResponseDTO {
    private String id;
    private String conversationId;
    private boolean hasAttachments;
    private String subject;
    private String bodyContent;
}
