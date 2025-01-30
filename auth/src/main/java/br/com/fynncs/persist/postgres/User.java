package br.com.fynncs.persist.postgres;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.persist.IUser;
import br.com.fynncs.persist.rowmapper.UserMapper;

import java.sql.SQLException;
import java.util.*;

public class User implements IUser {
    private final CRUDManager manager;

    public User(CRUDManager manager) {
        this.manager = manager;
    }

    private StringBuilder createTextSQL() {
        StringBuilder textSQL = new StringBuilder();

        textSQL.append("SELECT ");
        textSQL.append("u.id AS user_id, ");
        textSQL.append("u.login, ");
        textSQL.append("u.oauth_provider, ");
        textSQL.append("u.\"password\" ");
        textSQL.append("FROM ");
        textSQL.append("nentech.\"user\" u ");

        return textSQL;
    }

    @Override
    public Integer persist(br.com.fynncs.model.User user) throws SQLException, IllegalAccessException {
        return manager.persist(user);
    }

    @Override
    public br.com.fynncs.model.User findById(UUID id) throws SQLException {
        StringBuilder textSQL = this.createTextSQL();

        textSQL.append("WHERE u.id = ? ");
        return this.manager.queryObject(textSQL, id, new UserMapper());
    }

    @Override
    public br.com.fynncs.model.User findByLogin(String login, String oauthProvider) throws SQLException {
        StringBuilder textSQL = this.createTextSQL();
        List<String> params = new ArrayList<>();

        textSQL.append("WHERE ? = ANY(u.login) ");
        params.add(login);
        if (oauthProvider != null && !oauthProvider.isBlank()) {
            textSQL.append("AND u.oauth_provider = ? ");
            params.add(oauthProvider);
        }

        return this.manager.queryObject(textSQL, params, new UserMapper());
    }
}
