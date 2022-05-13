package net.caltona;

import com.google.inject.AbstractModule;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.sqlite.JDBC;

import javax.sql.DataSource;

public class Module extends AbstractModule {

    @SneakyThrows
    @Override
    protected void configure() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(JDBC.class.getName());
        dataSource.setJdbcUrl("jdbc:sqlite:database.sqlite");
        bind(DataSource.class).toInstance(dataSource);

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        bind(NamedParameterJdbcOperations.class).toInstance(jdbcTemplate);
    }

}
