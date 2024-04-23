package live.hi.services.ms365connector;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.microsoft.aad.msal4j.IAuthenticationResult;

import live.hi.services.ms365connector.dto.msgraph.UserListResponse;
import live.hi.services.ms365connector.service.MSGraphClient;
import lombok.ToString;
import lombok.extern.java.Log;

@SpringBootApplication
@ToString
@Log()
public class Ms365ConnectorApplication {
	private MSGraphClient msGraphClient; 
	private IAuthenticationResult authResult;
    
	public Ms365ConnectorApplication(
			MSGraphClient msGraphClient
			, IAuthenticationResult authResult ) {
		super();
		
		this.msGraphClient = msGraphClient;
		this.authResult = authResult;
	}

	public static void main(String[] args) {
		SpringApplication.run(Ms365ConnectorApplication.class, args);
	}

	// TODO: this is for quickly functionality evaluation.
	@Bean
	public CommandLineRunner startup() {
		return args -> {
			System.out.println("CommandLineRunner...");
			
			Map<String, String> headers = new HashMap<String, String>(); 
			headers.put( "Authorization", "Bearer " + authResult.accessToken() );
			headers.put( "Accept","application/json" );
			//System.out.println("token:"+authResult.accessToken());
						
			// UserListResponse response = msGraphClient.retrieveUsers( headers );
			// response.getValue().forEach( e -> log.info( e.toString() ) );
			
			System.out.println("CommandLineRunner finished!");
		};
	}
}
