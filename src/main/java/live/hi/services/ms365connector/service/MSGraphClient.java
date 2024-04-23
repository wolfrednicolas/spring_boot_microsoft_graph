package live.hi.services.ms365connector.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import live.hi.services.ms365connector.dto.msgraph.CopyDraftMessage;
import live.hi.services.ms365connector.dto.msgraph.CreateDraftMessage;
import live.hi.services.ms365connector.dto.msgraph.UploadSessionRequestDTO;
import live.hi.services.ms365connector.dto.msgraph.User;
import live.hi.services.ms365connector.dto.msgraph.UserListResponse;

public interface MSGraphClient {
	
	@GetExchange( "/v1.0/users" )
	UserListResponse retrieveUsers( @RequestHeader Map<String, String> headers );

	@GetExchange( "/v1.0/users/{id}")
	User getUser( @PathVariable String id, @RequestHeader Map<String, String> headers );

	@PostExchange( "/v1.0/users/{id}/messages" )
	ResponseEntity<?> createDraft( @PathVariable String id, @RequestHeader Map<String, String> headers, @RequestBody CreateDraftMessage message );

	@PostExchange( "/v1.0/users/{user_id}/messages/{draft_id}/copy" )
	ResponseEntity<?> copyDraft( @PathVariable String user_id, @RequestHeader Map<String, String> headers, @PathVariable String draft_id, @RequestBody CopyDraftMessage sent_item_folder);

	@PostExchange( "/v1.0/users/{user_id}/messages/{message_id}/send" )
	ResponseEntity<?> sendDraft( @PathVariable String user_id, @RequestHeader Map<String, String> headers, @PathVariable String message_id);

	@GetExchange( "/v1.0/users/{id}/mailFolders/Inbox/messages?$filter=isRead+eq+false" )
	ResponseEntity<?> notRead( @PathVariable String id, @RequestHeader Map<String, String> headers);

	@GetExchange( "/v1.0/users/{id}/mailFolders" )
	ResponseEntity<?> getFolders( @PathVariable String id, @RequestHeader Map<String, String> headers);

	@GetExchange( "/v1.0/users/{user_id}/messages/{message_id}/attachments" )
	ResponseEntity<?> getEmailAttachments( @PathVariable String user_id, @RequestHeader Map<String, String> headers, @PathVariable String message_id);

	@PostExchange( "/v1.0/users/{user_id}/messages/{draft_id}/attachments/createUploadSession" )
	ResponseEntity<?> createAnUploadSessionForMessage( @PathVariable String user_id, @RequestHeader Map<String, String> headers, @PathVariable String draft_id, @RequestBody UploadSessionRequestDTO uploadSessionRequestDTO);

	
}
