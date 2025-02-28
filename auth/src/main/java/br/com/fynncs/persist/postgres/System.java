package br.com.fynncs.persist.postgres;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.persist.ISystem;
import br.com.fynncs.persist.rowmapper.SystemMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class System implements ISystem {
    private final CRUDManager manager;

    public System(CRUDManager manager) {
        this.manager = manager;
    }

    private StringBuilder createTextSQL(Optional<List<String>> additionalFields) {
        StringBuilder textSQL = new StringBuilder();
        textSQL.append("SELECT ");
        textSQL.append("s.id, ");
        textSQL.append("s.\"name\", ");
        textSQL.append("s.\"database\", ");
        textSQL.append("s.connection_provider ");
        textSQL.append("FROM ");
        additionalFields.ifPresent(x -> textSQL.append(", ").append(x.parallelStream().collect(Collectors.joining(", "))));
        textSQL.append("nentech.\"system\" s ");

        return textSQL;
    }

    @Override
    public List<br.com.fynncs.model.System> findByUser(String userId) throws SQLException {
        StringBuilder textSQL = this.createTextSQL(Optional.empty());

        textSQL.append("INNER JOIN nentech.\"user_access\" ua ON ");
        textSQL.append("s.id = ua.system_id ");
        textSQL.append("WHERE ua.user_id = ? ");

        return this.manager.queryList(textSQL, userId.toString(), new SystemMapper());
    }

    @Override
    public List<br.com.fynncs.model.System> getAll() throws SQLException {
        StringBuilder textSQL = this.createTextSQL(Optional.empty());

        return this.manager.queryList(textSQL, null, new SystemMapper());
    }

    public int saveUserAccess(String userId, UUID systemId) throws SQLException {
        StringBuilder textSQL = new StringBuilder();
        textSQL.append("INSERT INTO\tnentech.user_access ( ");
        textSQL.append("user_id, ");
        textSQL.append("system_id, ");
        textSQL.append("access_level ");
        textSQL.append(") ");
        textSQL.append("VALUES(?, ?, 'U'::bpchar); ");

        PreparedStatement stmt = this.manager.prepareStatement(textSQL.toString());
        stmt.setString(1, userId);
        stmt.setString(2, systemId.toString());

        return stmt.executeUpdate();
    }
}
