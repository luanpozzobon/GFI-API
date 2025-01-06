package br.com.fynncs.email_service.persist.rowmapper;

import br.com.fynncs.core.interfaces.IRowMapper;
import br.com.fynncs.email_service.model.Email;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailMapper implements IRowMapper<Email> {
    @Override
    public Email createObject(ResultSet resultSet) throws SQLException {
        Email email = new Email();
        email.setType(resultSet.getString("type"));
        email.setBody(resultSet.getString("body"));
        return email;
    }
}
