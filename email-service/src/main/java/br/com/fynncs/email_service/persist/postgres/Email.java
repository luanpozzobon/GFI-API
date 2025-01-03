package br.com.fynncs.email_service.persist.postgres;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.email_service.persist.IEmail;
import br.com.fynncs.email_service.persist.rowmapper.EmailMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Email implements IEmail {

    private final CRUDManager manager;

    public Email(CRUDManager manager) {
        this.manager = manager;
    }

    private StringBuilder createTextSQL(Optional<List<String>> additionalFields) {
        StringBuilder textSQL = new StringBuilder();
        textSQL.append("select ");
        textSQL.append("type, ");
        textSQL.append("body ");
        additionalFields.ifPresent(x -> textSQL.append(", ").append(x.parallelStream().collect(Collectors.joining(", "))));
        textSQL.append(" from general.email em ");
        return textSQL;
    }

    @Override
    public br.com.fynncs.email_service.model.Email findByType(String type) throws SQLException {
        StringBuilder textSQL = createTextSQL(Optional.empty());
        textSQL.append(" WHERE em.type = ?");
        return manager.queryObject(textSQL, type, new EmailMapper());
    }
}
