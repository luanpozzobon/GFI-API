package br.com.fynncs.persist;

import br.com.fynncs.model.System;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ISystem {

    List<System> findByUser(UUID userId) throws SQLException;
}
