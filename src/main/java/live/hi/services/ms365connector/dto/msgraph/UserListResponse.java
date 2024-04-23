package live.hi.services.ms365connector.dto.msgraph;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserListResponse {
	@JsonProperty( "@odata.context" )
	private String context;
	List<User> value;	
}