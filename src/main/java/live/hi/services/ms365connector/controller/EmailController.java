package live.hi.services.ms365connector.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.msal4j.IAuthenticationResult;

import jakarta.validation.Valid;
import live.hi.services.ms365connector.dto.msgraph.CreateDraftMessageResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.DownloadFileListResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.DownloadFileResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.NotReadMessageResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.UpdateMessageRequestDTO;
import live.hi.services.ms365connector.dto.msgraph.UploadSessionRequestDTO;
import live.hi.services.ms365connector.dto.msgraph.UploadSessionResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.UserListResponse;
import live.hi.services.ms365connector.dto.msgraph.ValidationErrorResponse;
import live.hi.services.ms365connector.dto.msgraph.AttachmentItemDTO;
import live.hi.services.ms365connector.dto.msgraph.AttachmentRequestDTO;
import live.hi.services.ms365connector.dto.msgraph.AttachmentUploader;
import live.hi.services.ms365connector.dto.msgraph.CopyDraftMessage;
import live.hi.services.ms365connector.dto.msgraph.CreateDraftMessage;
import live.hi.services.ms365connector.service.MSGraphClient;
import lombok.NoArgsConstructor;


@RestController
@RequestMapping("es-email")
@NoArgsConstructor

public class EmailController {
	
	@Autowired
    private ObjectMapper objectMapper;

	@Value("${app.ms-graph.api.outlook_account_id}")
	private String outlookAccount;

	@Autowired
	private IAuthenticationResult authResult;

	@Autowired
	private MSGraphClient msGraphClient;
	
	@Autowired
	private Map<String, String> headers;

	@Value("${app.folders.sent_items}")
	private String sentItemsFolder;
	
	// Email API for ASES Enterprise System
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        ValidationErrorResponse errorResponse = new ValidationErrorResponse("Validation failed", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


	@GetMapping( path = "/getToken" )
	public ResponseEntity<?> getToken(){
		Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("token", authResult.accessToken());
		return ResponseEntity.status(200).body(jsonResponse);
	}
	
	@PostMapping( path = "/createDraft" )
	public ResponseEntity<?> createDraft(@Valid @RequestBody CreateDraftMessage createDraftMessage){
		ResponseEntity<?> responseEntity = msGraphClient.createDraft(outlookAccount, headers, createDraftMessage);
		CreateDraftMessageResponseDTO createDraftMessageResponseDTO = objectMapper.convertValue(responseEntity.getBody(), CreateDraftMessageResponseDTO.class);
		return ResponseEntity.status(responseEntity.getStatusCode()).body(createDraftMessageResponseDTO);
	}

	@PostMapping( path = "/copyDraft/{draft_id}/sentItemFolder" )
	public ResponseEntity<?> copyDraft(@PathVariable String draft_id) throws JsonProcessingException {
		String jsonString = "{\"destinationId\": \"" + sentItemsFolder + "\"}";
		ResponseEntity<?> responseEntity = msGraphClient.copyDraft(outlookAccount, headers, draft_id, copyDraftMessage(jsonString));
		return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
	}

	@PostMapping( path = "/sendDraft/{id}" )
	public ResponseEntity<?> sendEmailDrafted(@PathVariable String id) throws JsonProcessingException {
        ResponseEntity<?> responseEntity = msGraphClient.sendDraft(outlookAccount, headers, id);
		return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
	}

	@GetMapping( path = "/notRead" )
	public ResponseEntity<?> getNonReadEmails() throws JsonProcessingException {
        ResponseEntity<?> responseEntity = msGraphClient.notRead(outlookAccount, headers);
		
		JsonNode responseJson = objectMapper.convertValue(responseEntity.getBody(), JsonNode.class);

		JsonNode messagesArray = responseJson.get("value");

		List<NotReadMessageResponseDTO> messageSummaries = new ArrayList<>();

		for (JsonNode messageNode : messagesArray) {
            NotReadMessageResponseDTO messageSummary = new NotReadMessageResponseDTO();
            messageSummary.setId(messageNode.get("id").asText());
            messageSummary.setConversationId(messageNode.get("conversationId").asText());
            messageSummary.setHasAttachments(messageNode.get("hasAttachments").asBoolean());
            messageSummary.setSubject(messageNode.get("subject").asText());
            messageSummary.setBodyContent(messageNode.get("body").get("content").asText());
            messageSummaries.add(messageSummary);
        }
		return ResponseEntity.status(responseEntity.getStatusCode()).body(messageSummaries);
	}

	@GetMapping( path = "/messages/{id}/attachments" )
	@ResponseStatus(HttpStatus.OK)
	public DownloadFileListResponseDTO getEmailAttachments(@PathVariable String id) throws JsonProcessingException {
		DownloadFileListResponseDTO response = msGraphClient.getEmailAttachments(outlookAccount, headers, id);
		// Iterate over elements and adjust the name of each file
		for (DownloadFileResponseDTO file : response.getValue()) {
			file.setName(file.getName());
		}
		return response;
	}

	@GetMapping( path = "/messages/{message_id}/attachments/{attachment_id}" )
	@ResponseStatus(HttpStatus.OK)
	public DownloadFileResponseDTO getAttachmentById(@PathVariable String message_id, @PathVariable String attachment_id) throws JsonProcessingException {
		return msGraphClient.getAttachmentById(outlookAccount, headers, message_id, attachment_id);
	}

	private CopyDraftMessage copyDraftMessage(String jsonString) throws JsonMappingException, JsonProcessingException{
		CopyDraftMessage copyDraftMessage = objectMapper.readValue(jsonString, CopyDraftMessage.class);
        return copyDraftMessage;
	}

	@GetMapping( path = "/getFolders" )
	public ResponseEntity<?> getFolders() throws JsonProcessingException {
		ResponseEntity<?> responseEntity = msGraphClient.getFolders(outlookAccount, headers);
		return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
	}

	@PostMapping( path = "/messages/{draft_id}/attachment" )
	public ResponseEntity<?> uploadAttachment(@PathVariable String draft_id, @Valid @RequestBody AttachmentRequestDTO path) throws IOException {
		
		File file = new File(path.getPath());

		AttachmentItemDTO attachmentItemDTO = new AttachmentItemDTO("file", path.getPath(), file.length());
		UploadSessionRequestDTO uploadSessionRequestDTO = new UploadSessionRequestDTO(attachmentItemDTO);

		ResponseEntity<?> responseEntity = msGraphClient.createAnUploadSessionForMessage(outlookAccount, headers, draft_id, uploadSessionRequestDTO);

		UploadSessionResponseDTO uploadSessionResponseDTO = objectMapper.convertValue(responseEntity.getBody(), UploadSessionResponseDTO.class);

		AttachmentUploader uploader = new AttachmentUploader();
		int responseCode = uploader.uploadAttachment(uploadSessionResponseDTO.getUploadUrl(), file);
		return ResponseEntity.status(responseCode).body(null);
	}


	@PatchMapping( path = "/markAsRead/{message_id}" )
	public ResponseEntity<?> markAsRead(@PathVariable String message_id) throws JsonProcessingException {
		String jsonString = "{\"isRead\": \"" + true + "\"}";
		UpdateMessageRequestDTO updateMessageRequestDTO = objectMapper.readValue(jsonString, UpdateMessageRequestDTO.class);
		ResponseEntity<?> responseEntity = msGraphClient.updateMessage(outlookAccount, headers, message_id, updateMessageRequestDTO);
		return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
	}

	@GetMapping( path = "/retrieveUsers" ) //endpoint prueba
	public UserListResponse retrieveUsers() throws JsonProcessingException {
		return msGraphClient.retrieveUsers(headers);
	}

	public ResponseEntity<?> downloadFile(String messageId, String attachmentId, String fileName) throws IOException {
		URL url = new URL("https://graph.microsoft.com/v1.0/users/" + outlookAccount + "/messages/" + messageId + "/attachments/" + attachmentId + "/$value");
		HttpStatus status;
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + authResult.accessToken());

			ReadableByteChannel readableByteChannel = Channels.newChannel(connection.getInputStream());

			FileOutputStream fileOutputStream = new FileOutputStream(new File(System.getProperty("user.dir"),fileName)); 
			FileChannel fileChannel = fileOutputStream.getChannel();

			fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			fileChannel.close();
			fileOutputStream.close();
			connection.disconnect();
			status = HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		// Return the status
		return ResponseEntity.status(status).body(null);
	}
}
