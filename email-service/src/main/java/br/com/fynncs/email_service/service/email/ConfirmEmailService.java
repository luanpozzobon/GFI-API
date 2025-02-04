package br.com.fynncs.email_service.service.email;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.email_service.model.Email;
import br.com.fynncs.email_service.model.EmailType;
import br.com.fynncs.email_service.persist.CreatePersist;
import br.com.fynncs.email_service.persist.IEmail;
import br.com.fynncs.email_service.record.EmailUser;
import br.com.fynncs.email_service.service.interfaces.IEmailTypeAssembly;

import java.sql.SQLException;

public class ConfirmEmailService implements IEmailTypeAssembly<EmailUser> {

    private final CRUDManager manager;
    private IEmail emailPersist;

    public ConfirmEmailService(CRUDManager manager) throws Exception {
        this.manager = manager;
        initializer();
    }

    private void initializer() throws Exception {
        emailPersist = CreatePersist.createEmail(manager);
    }

    @Override
    public EmailType createEmailType(String system, EmailUser value) throws SQLException {
        Email email = emailPersist.findByType("CONFIRMEMAIL", system);
        EmailType emailType = new EmailType();
        emailType.setBody(body(email.getBody(), value, system));
        emailType.setSubject("Confirm Email!");
        emailType.setTo(value.email());
        return emailType;
    }

    private String body(String body, EmailUser value, String system) {
        body = body.replace("@ClientName", value.name());
        body = body.replace("@ConfirmLink", value.link());
        body = body.replace("@EnterpriseName", system);
        return body;
    }
}
