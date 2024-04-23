package live.hi.services.ms365connector.dto.msgraph;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadFileResponseDTO {
    private String name;
    private String contentType;
    private Long size;
    private String contentBytes;
}
