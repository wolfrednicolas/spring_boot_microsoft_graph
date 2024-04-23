package live.hi.services.ms365connector.dto.msgraph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadSessionResponseDTO {
    private String uploadUrl;
}
