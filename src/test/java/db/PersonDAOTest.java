package db;

import com.samhan.helloworld.HelloWorldApplication;
import com.samhan.helloworld.HelloWorldConfiguration;
import com.samhan.helloworld.api.Person;
import com.samhan.helloworld.db.PersonDAO;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.junit.DropwizardAppRule;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import java.sql.Connection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by shan on 21/12/2015.
 */
public class PersonDAOTest {
    @ClassRule
    public static final DropwizardAppRule<HelloWorldConfiguration> app =
            new DropwizardAppRule<HelloWorldConfiguration>(HelloWorldApplication.class,
                    PersonDAOTest.class.getResource("/test_config.yml").getPath());


    public static PersonDAO dao;

    @BeforeClass
    public static void ensureDbMigrated() {
        ManagedDataSource ds = app.getConfiguration().getDataSourceFactory().build(
                app.getEnvironment().metrics(),
                "migrations"
        );
        try {
            Connection connection = ds.getConnection();
            Liquibase migrator = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(connection));
            migrator.update("");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @BeforeClass
    public static void exposeDao() {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(app.getEnvironment(), app.getConfiguration().getDataSourceFactory(), "postgresql");
        PersonDAOTest.dao = jdbi.onDemand(PersonDAO.class);
    }

    @Before
    public void tidyDB() {
        dao.removeAll();
    }

    @Test
    public void testInsertAndFindPerson() {
        dao.insert(1, "tester", "password");

        Person person = dao.findByName("tester");

        assertThat(person.getId(), is(1));
        assertThat(person.getPassword(), is("password"));

    }

}
