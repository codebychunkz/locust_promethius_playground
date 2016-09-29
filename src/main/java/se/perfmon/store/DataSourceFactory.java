package se.perfmon.store;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory
{
    @Produces
    @Singleton
    public DataSource createDataSource()
    {
	HikariConfig config = new HikariConfig();
	config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
	config.addDataSourceProperty("URL", "jdbc:h2:tcp://127.0.0.1:9123/~/moviedb;MODE=MYSQL;INIT=RUNSCRIPT FROM 'classpath:init.sql'");
	config.setUsername("sa");
	config.setPassword("sa");
	config.setAutoCommit(false);
	config.setConnectionTimeout(5000);
	config.setMaximumPoolSize(20);

	HikariDataSource ds = new HikariDataSource(config);

	return ds;
    }
}
