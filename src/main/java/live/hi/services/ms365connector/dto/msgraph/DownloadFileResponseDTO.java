package live.hi.services.ms365connector.dto.msgraph;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DownloadFileResponseDTO {
    private String id;
    private String name;

    public String getName() {
        // Gets the file name without the path
        int lastSlashIndex = name.lastIndexOf("\\");
        if (lastSlashIndex != -1) {
            return name.substring(lastSlashIndex + 1);
        } else {
            return name; // If there is no route, return the full name
        }
    }
}
