package br.com.fynncs.persist.rowmapper;

import br.com.fynncs.core.interfaces.IRowMapper;
import br.com.fynncs.model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PersonMapper implements IRowMapper<Person> {
    @Override
    public Person createObject(ResultSet resultSet) throws SQLException {
        Person person = new Person();

        person.setUserId(UUID.fromString(resultSet.getString("user_id")));
        person.setNickname(resultSet.getString("nickname"));
        person.setName(resultSet.getString("name"));
        person.setBirthday(resultSet.getDate("birthday").toLocalDate());
        person.setGender(resultSet.getString("gender"));
        person.setNationality(resultSet.getString("nationality"));
        person.setMaritalStatus(resultSet.getString("marital_status"));
        person.setProfession(resultSet.getString("profession"));
        person.setEducation(resultSet.getString("education"));

        return person;
    }
}
