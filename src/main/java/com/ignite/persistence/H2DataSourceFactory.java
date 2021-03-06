package com.ignite.persistence;

import java.sql.Connection;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 * Datasource to use.
 * https://github.com/apache/ignite/blob/master/modules/core/src/test/java/org/apache/ignite/cache/store/jdbc/H2DataSourceFactory.java
 */
public class H2DataSourceFactory implements Factory<DataSource> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8932237228334074493L;

	/** DB connection URL. */
	private static final String DFLT_CONN_URL = "jdbc:h2:tcp://localhost/~/data/seviper";
	//private static final String DFLT_CONN_URL = "jdbc:h2:file:/D:/data/seviper;FILE_LOCK=NO";
	/** Instance */
	private static H2DataSourceFactory instance;

	/**
	 * Singleton Instance
	 * 
	 * @return
	 */
	public static H2DataSourceFactory getInstance() {
		if (instance == null) {
			instance = new H2DataSourceFactory();
		}
		return instance;
	}

	/** {@inheritDoc} */
	@Override
	public DataSource create() {
		return JdbcConnectionPool.create(DFLT_CONN_URL, "admin", "Asdf963.");
	}

	public Connection getConnection() {
		try {
			Connection conn = this.create().getConnection();
			return conn;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}