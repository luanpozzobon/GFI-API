package br.com.fynncs.persist;

import br.com.fynncs.model.User;

import java.sql.SQLException;
import java.util.UUID;

public interface IUser {

    Integer persist(User user) throws SQLException, IllegalAccessException;

    User findById(UUID id) throws SQLException;

    User findByLogin(String login, String oauthProvider) throws SQLException;
}
