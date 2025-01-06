package br.com.fynncs.email_service.persist;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.email_service.persist.postgres.Email;

public class CreatePersist {

    public static IEmail createEmail(CRUDManager manager) throws Exception {
        switch (manager.getProvider()) {
            case POSTGRES -> {
                return new Email(manager);
            }
            default -> throw new Exception("Connection to provider not developed");
        }
    }
}
