package br.com.fynncs.email_service.controller;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.core.enums.ConnectionProvider;
import br.com.fynncs.core.enums.ResourceType;
import br.com.fynncs.core.model.Resource;
import br.com.fynncs.core.service.ResourceService;
import br.com.fynncs.email_service.kafka.KafkaConsumer;
import br.com.fynncs.email_service.kafka.KafkaProducer;
import br.com.fynncs.email_service.model.EmailType;
import br.com.fynncs.email_service.record.EmailUser;
import br.com.fynncs.email_service.record.User;
import br.com.fynncs.email_service.service.email.ConfirmEmailService;
import br.com.fynncs.email_service.service.email.EmailResetPasswordService;
import br.com.fynncs.email_service.service.interfaces.IEmailTypeAssembly;
import br.com.fynncs.security.model.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RequestMapping("/api/emailService")
@RestController
public class EmailServiceController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/sendResetPassword")
    public ResponseEntity<String> sendEmail(HttpServletRequest request, @RequestBody User user) throws Exception {
        Authentication authentication = Authentication.deserialize(((Principal) request.getAttribute("userPrincipal")).getName());
        try (CRUDManager manager = createCrudManager(authentication, "STANDARD", ResourceType.DATABASECONNECTION)) {
            final String id = "RESETPASSWORD";
            EmailType emailType = createEmailType(manager, user, authentication, id, null);
            kafkaProducer.send("email", emailType);
            return ResponseEntity.ok("Success");
        } catch (Exception ex) {
            throw new Exception("Error send email reset password", ex);
        }
    }

    @PostMapping("/sendConfirmEmail")
    public ResponseEntity<String> sendConfirmEmail(HttpServletRequest request, @RequestBody User user) throws Exception {
        Authentication authentication = Authentication.deserialize(((Principal) request.getAttribute("userPrincipal")).getName());
        try (CRUDManager manager = createCrudManager(authentication, "STANDARD", ResourceType.DATABASECONNECTION)) {
            String transactionID = UUID.randomUUID().toString();
            final String id = "CONFIRMEMAIL";
            EmailType emailType = createEmailType(manager, user, authentication, id,
                    createLink(authentication, id) + "/" + transactionID);
            kafkaProducer.sendConfirmEmail(transactionID, user);
            kafkaProducer.send("email", emailType);
            return ResponseEntity.ok("Success");
        } catch (Exception ex) {
            throw new Exception("Error send email reset password", ex);
        }
    }

    @GetMapping("/confirm/{id}")
    public ResponseEntity<String> sendConfirmEmail(HttpServletRequest request, @PathVariable String id) throws Exception {
        Authentication authentication = Authentication.deserialize(((Principal) request.getAttribute("userPrincipal")).getName());
        try {
            KafkaConsumer.consumerTransaction(id, kafkaProducer, User.class);
            return ResponseEntity.ok("Success");
        } catch (Exception ex) {
            throw new Exception("Error send email reset password", ex);
        }
    }

    private CRUDManager createCrudManager(Authentication authentication, String id, ResourceType type) throws Exception {
        return new CRUDManager(
                ResourceService.findResourceById(
                        ConnectionProvider.valueOf(authentication.getConnectionProvider()),
                        id, type),
                ConnectionProvider.valueOf(authentication.getConnectionProvider()));
    }

    private Resource createResource(Authentication authentication, String id, ResourceType type) throws Exception {
        return ResourceService.findResourceById(ConnectionProvider.valueOf(authentication.getConnectionProvider()), id, type);
    }

    private String createLink(Authentication authentication, String id) throws Exception {
        Resource resource = createResource(authentication, id, ResourceType.URLWEB);
        return resource.getInfo().get(ResourceService.getEnvironment().concat("_")
                .concat(authentication.getSystem()));
    }

    private EmailUser createEmailUser(User user, String link) {
        return new EmailUser(user.name(), user.email(), link);
    }

    private EmailType createEmailType(CRUDManager manager, User user, Authentication authentication, String id, String link) throws Exception {
        IEmailTypeAssembly<EmailUser> assembly = switch (id) {
            case "CONFIRMEMAIL" -> createConfirmEmailType(manager);
            case "RESETPASSWORD" -> createResetPassWordEmailType(manager);
            default -> throw new Exception("Tipo invalido");
        };
        return assembly.createEmailType(authentication.getSystem(),
                createEmailUser(user, link));
    }

    private IEmailTypeAssembly<EmailUser> createResetPassWordEmailType(CRUDManager manager) throws Exception {
        return new EmailResetPasswordService(manager);
    }

    private IEmailTypeAssembly<EmailUser> createConfirmEmailType(CRUDManager manager) throws Exception {
        return new ConfirmEmailService(manager);
    }

}
