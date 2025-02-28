package br.com.fynncs.persist.rowmapper;

import br.com.fynncs.core.enums.State;
import br.com.fynncs.core.interfaces.IRowMapper;
import br.com.fynncs.core.proxy.ProxyModel;
import br.com.fynncs.model.Person;
import br.com.fynncs.model.User;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class UserMapper implements IRowMapper<User> {
    @Override
    public User createObject(ResultSet resultSet) throws SQLException {
//        User user = ProxyModel.createProxy(new User());
        User user = new User();
        user.setId(resultSet.getString("id"));

        Array loginSQL = resultSet.getArray("login");
        if (loginSQL != null) {
            String[] login = (String[]) loginSQL.getArray();
            user.setLogin(Arrays.asList(login));
        }

        user.setOauthProvider(resultSet.getString("oauth_provider"));
        user.setPassword(resultSet.getString("password"));
        user.setPerson(new Person());
        user.getPerson().setUserId(UUID.fromString(user.getId()));

//        user.setState(State.ORIGINAL);
        return user;
    }
}
