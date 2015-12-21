package com.samhan.helloworld.db;

import com.samhan.helloworld.api.Person;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements ResultSetMapper<Person> {

    public Person map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Person(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("password"));
    }
}
