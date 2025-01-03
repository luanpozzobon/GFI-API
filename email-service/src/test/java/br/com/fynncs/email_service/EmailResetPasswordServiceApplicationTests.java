package br.com.fynncs.email_service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.core.enums.ConnectionProvider;
import br.com.fynncs.core.enums.ResourceType;
import br.com.fynncs.core.service.ResourceService;
import br.com.fynncs.email_service.kafka.KafkaProducer;
import br.com.fynncs.email_service.service.email.type.EmailResetPasswordService;
import br.com.fynncs.email_service.service.interfaces.IEmailTypeAssembly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailResetPasswordServiceApplicationTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    void contextLoads() throws Exception {
        IEmailTypeAssembly<String> emailType = new EmailResetPasswordService<>(new CRUDManager(
                ResourceService.findResourceById(ConnectionProvider.POSTGRES, "NOTIFICATION", ResourceType.DATABASECONNECTION),
                ConnectionProvider.POSTGRES, "notification"));
//        kafkaProducer.send("email", emailType.createEmailType("RESETPASSWORD", ""));
    }

}
