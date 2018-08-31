package com.ignite;

import java.sql.Types;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.H2Dialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.ignite.model.Student;
import com.ignite.persistence.H2DataSourceFactory;

/**
 * https://apacheignite.readme.io/docs/getting-started
 * https://dzone.com/articles/in-memory-data-grid-with-apache-ignite
 * 
 * * Details of previous configurations on the IgniteServerNode project on my
 * github
 * 
 * @author jcamargos
 *
 */
public class AppConfig {

	public static void main(String[] args) {
		IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
		igniteConfiguration.setClientMode(false);

		CacheConfiguration<Long, Student> cacheConfig = new CacheConfiguration<>();

		/**
		 * https://apacheignite.readme.io/docs/3rd-party-store#section-read-through-and-write-through
		 */
		// Read data not found in a cache from the database
		cacheConfig.setReadThrough(true);
		// Enable export cache updates to the database
		cacheConfig.setWriteThrough(true);

		cacheConfig.setName("StudentCache");
		cacheConfig.setAtomicityMode(CacheAtomicityMode.ATOMIC);
		cacheConfig.setBackups(1);

		// DS Factory for the Caches
		H2DataSourceFactory dsFactory = H2DataSourceFactory.getInstance();

		CacheJdbcPojoStoreFactory<Object, Object> storeFactory = new CacheJdbcPojoStoreFactory<>();
		storeFactory.setDataSourceFactory(dsFactory);
		storeFactory.setDialect(new H2Dialect());
		storeFactory.setTypes(getTypes());

		cacheConfig.setCacheStoreFactory(storeFactory);

		// queryEntities on annotations over the Student class
		igniteConfiguration.setCacheConfiguration(cacheConfig);

		Ignition.start(igniteConfiguration);
	}

	/**
	 * Map tables for Ignite
	 * 
	 * @return
	 */
	private static JdbcType[] getTypes() {
		JdbcType[] types = new JdbcType[1];

		// Student table
		types[0] = new JdbcType();
		types[0].setCacheName("StudentCache");
		types[0].setKeyType(Long.class);
		types[0].setValueType(Student.class);
		types[0].setDatabaseTable("STUDENT");

		// Primary key of STUDENT table
		JdbcTypeField[] studentKeys = new JdbcTypeField[1];
		studentKeys[0] = new JdbcTypeField(Types.BIGINT, "STUDENT_ID", Long.class, "id");

		types[0].setKeyFields(studentKeys);

		// Fields aside from the PK of the STUDENT table
		JdbcTypeField[] studentValues = new JdbcTypeField[2];
		studentValues[0] = new JdbcTypeField(Types.VARCHAR, "STUDENT_NAME", String.class, "name");
		studentValues[1] = new JdbcTypeField(Types.DOUBLE, "STUDENT_AVG", Double.class, "avg");

		types[0].setValueFields(studentValues);

		return types;
	}
}