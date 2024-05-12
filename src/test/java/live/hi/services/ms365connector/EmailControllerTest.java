package live.hi.services.ms365connector;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import live.hi.services.ms365connector.controller.EmailController;
import live.hi.services.ms365connector.dto.msgraph.AttachmentRequestDTO;
import live.hi.services.ms365connector.dto.msgraph.Body;
import live.hi.services.ms365connector.dto.msgraph.CreateDraftMessage;
import live.hi.services.ms365connector.dto.msgraph.CreateDraftMessageResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.DownloadFileListResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.DownloadFileResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.EmailAddress;
import live.hi.services.ms365connector.dto.msgraph.NotReadMessageResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.Recipient;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmailControllerTest {

    @Autowired
    private EmailController emailController;

    private static String draftId;

    private static String messageId;

    private static final String dummyText = "src\\test\\java\\live\\hi\\services\\ms365connector\\hello_world.txt";

    private static String attachmentId;
    private static String name;

    public CreateDraftMessage createDrafMessageFunction(){
        String subject = "subject";
        String importance = "low";

        Body body = new Body("text", "contenido");
        List<Recipient> toRecipients = new ArrayList<>();
        Recipient toRecipient = new Recipient(new EmailAddress("mailtest@asespr.org"));
        toRecipients.add(toRecipient);
        return new CreateDraftMessage(
            subject, body, toRecipients,new ArrayList<>(), importance);
    }

    @Test
    @Order(1)
    public void createDrafMessageOk() throws Exception {
        ResponseEntity<?> responseEntity = emailController.createDraft(createDrafMessageFunction());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        CreateDraftMessageResponseDTO output = (CreateDraftMessageResponseDTO) responseEntity.getBody();
        assertTrue(output.getId() instanceof  String);
        assertTrue(output.getConversationId() instanceof  String);
        draftId = output.getId();
    }

    @Test
    @Order(2)
    public void copyDraftMessageToSentItemFolder() throws Exception {
        ResponseEntity<?> responseEntity = emailController.copyDraft(draftId );
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    public void attachtFileToDraft() throws Exception {
        AttachmentRequestDTO body = new AttachmentRequestDTO();
        body.setPath(dummyText);
        ResponseEntity<?> responseEntity = emailController.uploadAttachment(draftId, body);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    public void sendDraft() throws Exception {
        ResponseEntity<?> responseEntity = emailController.sendEmailDrafted(draftId );
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        Thread.sleep(2000); // Time elapsed while sending the email
    }

    @SuppressWarnings("unchecked")
    @Test
    @Order(5)
    public void getNotReadEmails() throws Exception {
        ResponseEntity<?> responseEntity = emailController.getNonReadEmails();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<NotReadMessageResponseDTO> messageSummaries = (List<NotReadMessageResponseDTO>) responseEntity.getBody();
        assertTrue(messageSummaries.get(0).getId() instanceof  String);
        messageId = messageSummaries.get(0).getId();
    }

    @Test
    @Order(6)
    public void markAsRead() throws Exception {
        ResponseEntity<?> responseEntity = emailController.markAsRead(messageId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(6)
    public void getEmailAttachments() throws Exception {
        DownloadFileListResponseDTO responseEntity = emailController.getEmailAttachments(messageId);
        assertEquals(responseEntity.getValue().get(0).getName(), "hello_world.txt");
        assertTrue(responseEntity.getValue().get(0).getName() instanceof  String);
        assertTrue(responseEntity.getValue().get(0).getId() instanceof  String);
        attachmentId = responseEntity.getValue().get(0).getId();
        name = responseEntity.getValue().get(0).getName();
    }

    @Test
    @Order(7)
    public void getAttachmentById() throws Exception {
        DownloadFileResponseDTO responseEntity = emailController.getAttachmentById(messageId, attachmentId);
        assertTrue(responseEntity.getName() instanceof  String);
        assertTrue(responseEntity.getId() instanceof  String);
        name = responseEntity.getName();
    }

    @Test
    @Order(8)
    public void downloadFile() throws Exception {
        ResponseEntity<?> responseEntity = emailController.downloadFile(messageId, attachmentId, name);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        File tempFile = new File(System.getProperty("user.dir"), name);
        tempFile.delete();
    }

}
