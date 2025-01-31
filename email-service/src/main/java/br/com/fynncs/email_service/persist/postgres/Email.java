package br.com.fynncs.email_service.persist.postgres;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.email_service.persist.IEmail;
import br.com.fynncs.email_service.persist.rowmapper.EmailMapper;

import java.sql.SQLException;
import java.util.Arrays;
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
        textSQL.append(" from parameter.email em ");
        return textSQL;
    }

    @Override
    public br.com.fynncs.email_service.model.Email findByType(String type, String system) throws SQLException {
        StringBuilder textSQL = createTextSQL(Optional.empty());
        textSQL.append(" WHERE em.type ilike ? and em.system ilike ?");
        return manager.queryObject(textSQL, Arrays.asList(type, system), new EmailMapper());
    }
}
