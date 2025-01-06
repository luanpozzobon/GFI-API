package br.com.fynncs.persist;

import br.com.fynncs.model.User;

import java.sql.SQLException;

public interface IUser {

    boolean persist(User user) throws SQLException, IllegalAccessException;
}
