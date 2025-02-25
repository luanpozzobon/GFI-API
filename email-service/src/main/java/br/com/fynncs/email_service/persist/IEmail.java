package br.com.fynncs.email_service.persist;

import br.com.fynncs.email_service.model.Email;

import java.sql.SQLException;

public interface IEmail {

    Email findByType(String type, String system) throws SQLException;
}
