package br.com.fynncs.persist;

import br.com.fynncs.model.System;
import br.com.fynncs.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ISystem {

    List<System> findByUser(String userId) throws SQLException;

    List<System> getAll() throws SQLException;

    int saveUserAccess(String userId, UUID systemId) throws SQLException;
}
