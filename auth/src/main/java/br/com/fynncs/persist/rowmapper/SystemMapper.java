package br.com.fynncs.persist.rowmapper;

import br.com.fynncs.core.enums.State;
import br.com.fynncs.core.interfaces.IRowMapper;
import br.com.fynncs.core.proxy.ProxyModel;
import br.com.fynncs.model.System;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SystemMapper implements IRowMapper<System> {
    @Override
    public System createObject(ResultSet resultSet) throws SQLException {
//        System system = ProxyModel.createProxy(new System());
        System system = new System();

        system.setId(UUID.fromString(resultSet.getString("id")));
        system.setName(resultSet.getString("name"));
        system.setDatabase(resultSet.getString("database"));
        system.setConnectionProvider(resultSet.getString("connection_provider"));

//        system.setState(State.ORIGINAL);
        return system;
    }
}
