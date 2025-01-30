package br.com.fynncs.persist;

import br.com.fynncs.core.connection.CRUDManager;

public abstract class CreatePersist {
    public static IUser createUser(CRUDManager manager) throws Exception {
        switch (manager.getProvider()) {
            case POSTGRES -> {
                return new br.com.fynncs.persist.postgres.User(manager);
            }
            default -> throw new Exception("Connection to provider not developed");
        }
    }
}
