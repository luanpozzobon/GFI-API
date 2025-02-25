package br.com.fynncs.email_service;

import br.com.fynncs.core.comum.AbstractTable;
import br.com.fynncs.core.config.AbstractTableConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(AbstractTableConfig.class)
class EmailServiceApplicationTests {

    //    @Autowired
//    private KafkaProducer kafkaProducer;
    @Autowired
    private AbstractTable table;

    @Test
    void contextLoads() throws Exception {
//        IEmailTypeAssembly<Client> emailType = new EmailResetPasswordService(new CRUDManager(
//                ResourceService.findResourceById(ConnectionProvider.POSTGRES, "NOTIFICATION", ResourceType.DATABASECONNECTION),
//                ConnectionProvider.POSTGRES, "notification"));
//        kafkaProducer.send("email", emailType.createEmailType("RESETPASSWORD", new Client("MIKE", "miketesteemail@gmail.com", "122")));
//        while (true) {

//        }
    }

}
