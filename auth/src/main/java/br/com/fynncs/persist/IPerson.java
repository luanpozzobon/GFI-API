package br.com.fynncs.persist;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.Person;

import java.sql.SQLException;
import java.util.UUID;

public interface IPerson {

    Integer persist(Person person) throws SQLException, IllegalAccessException;

    Person findById(UUID id) throws SQLException;
}
