package live.hi.services.ms365connector.config;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;

import live.hi.services.ms365connector.service.MSGraphClient;
import lombok.ToString;
import lombok.extern.java.Log;

@Configuration
@ToString
@Log
public class MS365ConnectorConfig {
	private String authority;
	private String clientId;
	private String secret;
	private String scope;
	
	public MS365ConnectorConfig(
			@Value( "${app.msal.authority}" ) String authority
			, @Value( "${app.msal.client_id}" ) String clientId
			, @Value( "${app.msal.secret}" ) String secret
			, @Value( "${app.msal.scope}" ) String scope ) {
		super();
		
		this.authority = authority;
		this.clientId = clientId;
		this.secret = secret;
		this.scope = scope;
	}

	@Bean
	public MSGraphClient msGraphClient( @Value( "${app.ms-graph.api.baseurl}" ) String baseurl) {
		WebClient client = 
				WebClient.builder()
                	.baseUrl( baseurl )
                	.build();
		
		WebClientAdapter adapter = WebClientAdapter.create( client );
		adapter.setBlockTimeout( Duration.ofSeconds( 30 ) );
		
        HttpServiceProxyFactory factory = 
        		HttpServiceProxyFactory.builderFor( adapter ).build();
        
        return factory.createClient( MSGraphClient.class );
	} 
	
	@Bean("msGraphAccessToken")
	public IAuthenticationResult msGraphAccessToken() throws Exception {
		log.info( "requesting new access-token" );
		
		ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                clientId,
                ClientCredentialFactory.createFromSecret(secret))
                	.authority(authority)
                	.build();

		ClientCredentialParameters clientCredentialParam = ClientCredentialParameters
				.builder(Collections.singleton(scope)).build();

		CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
		return future.get();
	}

	@Bean
	@DependsOn({"msGraphAccessToken"})
	public Map<String, String> prepareHeaders(IAuthenticationResult authResult) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + authResult.accessToken());
        headers.put("Accept", "application/json");
        return headers;
    }
	
}
