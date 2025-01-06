package br.com.fynncs.persist.rowmapper;

import br.com.fynncs.core.interfaces.IRowMapper;
import br.com.fynncs.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserMapper implements IRowMapper<User> {
    @Override
    public User createObject(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(UUID.fromString(resultSet.getString("id")));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        user.setGender(resultSet.getString("gender"));
        user.setNationality(resultSet.getString("nationality"));
        user.setMaritalStatus(resultSet.getString("marital_status"));
        user.setProfession(resultSet.getString("profession"));
        user.setAcademicBackground(resultSet.getString("academic_background"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));

        return user;
    }
}
