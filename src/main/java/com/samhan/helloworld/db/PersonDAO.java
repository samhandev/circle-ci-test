package com.samhan.helloworld.db;

import com.google.common.collect.ImmutableList;
import com.samhan.helloworld.api.Person;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PersonMapper.class)
public interface PersonDAO {
    @SqlUpdate("create table people(id int primary key, name varchar(100))")
    void createPersonTable();

    @SqlUpdate("insert into people (id, name, password) values (:id, :name, :password)")
    void insert(@Bind("id") int id, @Bind("name") String name, @Bind("password") String password);

    @SqlQuery("select name from people where id = :id")
    String findNameById(@Bind("id") int id);

    @SqlQuery("select id, name, password from people")
    ImmutableList<Person> findAll();

    @SqlQuery("select id, name, password from people where name = :name")
    Person findByName(@Bind("name") String name);

    @SqlUpdate("DELETE FROM people")
    int removeAll();
}


