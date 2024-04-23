package live.hi.services.ms365connector.dto.msgraph;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
	private String id;
	private String displayName;
	private String givenName;
	private String surname;
	private String mail;
}
