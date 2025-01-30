package br.com.fynncs.persist;

import br.com.fynncs.core.connection.CRUDManager;

public abstract class CreatePersist {
    public static IUser createUser(CRUDManager manager) throws Exception {
        switch (manager.getProvider()) {
            case POSTGRES -> {
                return new br.com.fynncs.persist.postgres.User(manager);
            }
            case null -> throw new Exception("Connection provider must not be null!");
            default -> throw new Exception("Connection to provider not developed");
        }
    }

    public static IPerson createPerson(CRUDManager manager) throws Exception {
        switch (manager.getProvider()) {
            case POSTGRES -> {
                return new br.com.fynncs.persist.postgres.Person(manager);
            }
            case null -> throw new Exception("Connection provider must not be null!");
            default -> throw new Exception("Connection to provider not developed");
        }
    }

    public static ISystem createSystem(CRUDManager manager) throws Exception {
        switch (manager.getProvider()) {
            case POSTGRES -> {
                return new br.com.fynncs.persist.postgres.System(manager);
            }
            case null -> throw new Exception("Connection provider must not be null!");
            default -> throw new Exception("Connection to provider not developed");
        }
    }
}
