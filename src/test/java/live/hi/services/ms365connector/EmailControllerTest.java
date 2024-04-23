package live.hi.services.ms365connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import live.hi.services.ms365connector.controller.EmailController;
import live.hi.services.ms365connector.dto.msgraph.Body;
import live.hi.services.ms365connector.dto.msgraph.CreateDraftMessage;
import live.hi.services.ms365connector.dto.msgraph.CreateDraftMessageResponseDTO;
import live.hi.services.ms365connector.dto.msgraph.EmailAddress;
import live.hi.services.ms365connector.dto.msgraph.Recipient;
import live.hi.services.ms365connector.service.MSGraphClient;

@SpringBootTest
public class EmailControllerTest {

    @Autowired
    private EmailController emailController;

    @Mock
    private MSGraphClient msGraphClient;

    
    public static CreateDraftMessage createDrafMessageFunction(){
        String subject = "subject";
        String importance = "low";

        Body body = new Body("text", "contenido");
        List<Recipient> toRecipients = new ArrayList<>();
        Recipient toRecipient = new Recipient(new EmailAddress("ejemplo@example.com"));
        toRecipients.add(toRecipient);
        return new CreateDraftMessage(
            subject, body, toRecipients,new ArrayList<>(), importance);
    }

    @Test
    public void createDrafMessageOk() throws Exception {
        CreateDraftMessage createDraftMessage = createDrafMessageFunction();
        ResponseEntity<?> responseEntity = emailController.createDraft(createDraftMessage);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

}
