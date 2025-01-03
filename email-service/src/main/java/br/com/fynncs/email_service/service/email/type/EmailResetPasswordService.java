package br.com.fynncs.email_service.service.email.type;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.email_service.model.Email;
import br.com.fynncs.email_service.model.EmailType;
import br.com.fynncs.email_service.persist.CreatePersist;
import br.com.fynncs.email_service.persist.IEmail;
import br.com.fynncs.email_service.service.interfaces.IEmailTypeAssembly;

import java.sql.SQLException;

public class EmailResetPasswordService<T> implements IEmailTypeAssembly<T> {

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
    public EmailType createEmailType(String type, T value) throws SQLException {
        Email email = emailPersist.findByType(type);
        EmailType emailType = new EmailType();
        emailType.setBody(email.getBody());
        emailType.setSubject("Reset Password!");
        emailType.setTo((String) value);
        return emailType;
    }
}
