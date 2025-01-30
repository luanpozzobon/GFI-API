package br.com.fynncs.persist.postgres;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.persist.IUser;

import java.sql.SQLException;

public class User implements IUser {
    private final CRUDManager manager;

    public User(CRUDManager manager) {
        this.manager = manager;
    }

    @Override
    public Integer persist(br.com.fynncs.model.User user) throws SQLException, IllegalAccessException {
        return manager.persist(user);
    }
}
