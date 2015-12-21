package com.samhan.helloworld;

import com.samhan.helloworld.db.PersonDAO;
import com.samhan.helloworld.health.TemplateHealthCheck;
import com.samhan.helloworld.resources.HelloWorldResource;
import com.samhan.helloworld.resources.PersonResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

/**
 * Created by shan on 21/12/2015.
 */
public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addBundle(new Java8Bundle());
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<HelloWorldConfiguration>() {
            public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final PersonDAO dao = jdbi.onDemand(PersonDAO.class);

        final HelloWorldResource resource = new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName());
        final PersonResource personResource = new PersonResource(dao);

        environment.jersey().register(resource);
        environment.jersey().register(personResource);
        environment.healthChecks().register("template", healthCheck);
    }
}
