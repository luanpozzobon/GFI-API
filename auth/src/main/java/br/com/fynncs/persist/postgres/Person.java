package br.com.fynncs.persist.postgres;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.persist.IPerson;
import br.com.fynncs.persist.rowmapper.PersonMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Person implements IPerson {
    private final CRUDManager manager;

    public Person(CRUDManager manager) {
        this.manager = manager;
    }

    private StringBuilder createTextSQL(Optional<List<String>> additionalFields) {
        StringBuilder textSQL = new StringBuilder();

        textSQL.append("SELECT ");
        textSQL.append("user_id, ");
        textSQL.append("nickname, ");
        textSQL.append("\"name\", ");
        textSQL.append("birthday, ");
        textSQL.append("email, ");
        textSQL.append("gender, ");
        textSQL.append("nationality, ");
        textSQL.append("marital_status, ");
        textSQL.append("profession, ");
        textSQL.append("education ");
        additionalFields.ifPresent(x -> textSQL.append(", ").append(x.parallelStream().collect(Collectors.joining(", "))));
        textSQL.append("FROM ");
        textSQL.append("lpz.person p ");

        return textSQL;
    }

    @Override
    public Integer persist(br.com.fynncs.model.Person person) throws SQLException, IllegalAccessException {
        return this.manager.persist(person);
    }

    @Override
    public br.com.fynncs.model.Person findById(UUID id) throws SQLException {
        StringBuilder textSQL = this.createTextSQL(Optional.empty());

        textSQL.append("WHERE p.id = ?");
        return this.manager.queryObject(textSQL, id, new PersonMapper());
    }
}
