package br.com.fynncs.email_service.controller;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.core.enums.ConnectionProvider;
import br.com.fynncs.core.enums.ResourceType;
import br.com.fynncs.core.service.ResourceService;
import br.com.fynncs.email_service.kafka.KafkaProducer;
import br.com.fynncs.email_service.model.EmailType;
import br.com.fynncs.email_service.record.Client;
import br.com.fynncs.email_service.service.email.fynncs.EmailResetPasswordService;
import br.com.fynncs.email_service.service.interfaces.IEmailTypeAssembly;
import br.com.fynncs.security.model.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequestMapping("/api/emailService")
@RestController
public class EmailServiceController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/fynncs/sendResetPassword")
    public ResponseEntity<String> sendEmail(HttpServletRequest request, @RequestBody Client client) throws Exception {
        Authentication authentication = Authentication.deserialize(((Principal) request.getAttribute("userPrincipal")).getName());
        try (CRUDManager manager = new CRUDManager(
                ResourceService.findResourceById(ConnectionProvider.valueOf(authentication.getConnectionProvider()),
                        "NOTIFICATION", ResourceType.DATABASECONNECTION),
                ConnectionProvider.valueOf(authentication.getConnectionProvider()))) {
            IEmailTypeAssembly<Client> assembly = new EmailResetPasswordService(manager);
            EmailType emailType = assembly.createEmailType("RESETPASSWORD", "fynncs", client);
            kafkaProducer.send("email", emailType);
            return ResponseEntity.ok("Success");
        } catch (Exception ex) {
            throw new Exception("Error send email reset password", ex);
        }
    }
}
