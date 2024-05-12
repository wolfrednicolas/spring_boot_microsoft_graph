package live.hi.services.ms365connector.dto.msgraph;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DownloadFileListResponseDTO {
	private List<DownloadFileResponseDTO> value;	
}