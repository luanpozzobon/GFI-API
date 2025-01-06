package br.com.fynncs.email_service.service.email.fynncs;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.email_service.model.Email;
import br.com.fynncs.email_service.model.EmailType;
import br.com.fynncs.email_service.persist.CreatePersist;
import br.com.fynncs.email_service.persist.IEmail;
import br.com.fynncs.email_service.record.Client;
import br.com.fynncs.email_service.service.interfaces.IEmailTypeAssembly;

import java.sql.SQLException;

public class EmailResetPasswordService implements IEmailTypeAssembly<Client> {

    private final CRUDManager manager;
    private IEmail emailPersist;

    public EmailResetPasswordService(CRUDManager manager) throws Exception {
        this.manager = manager;
        initializer();
    }

    private void initializer() throws Exception {
        emailPersist = CreatePersist.createEmail(manager);
    }

    @Override
    public EmailType createEmailType(String type, String enterprise, Client value) throws SQLException {
        Email email = emailPersist.findByType(type, enterprise);
        EmailType emailType = new EmailType();
        emailType.setBody(body(email.getBody(), value));
        emailType.setSubject("Reset Password!");
        emailType.setTo(value.email());
        return emailType;
    }

    private String body(String body, Client value) {
        body = body.replace("@ClientName", value.name());
        body = body.replace("@ResetPasswordLink", value.name());
        return body;
    }
}
