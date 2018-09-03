package com.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.dialect.H2Dialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import com.ignite.model.Student;
import com.ignite.persistence.H2DataSourceFactory;
import com.ignite.utilities.IgniteAutoConfig;

/**
 * https://apacheignite.readme.io/docs/getting-started
 * https://dzone.com/articles/in-memory-data-grid-with-apache-ignite
 * 
 * * Details of previous configurations on the IgniteServerNode project on my
 * github
 * 
 * STUDENT TABLE inserts on data.sql
 * 
 * @author jcamargos
 *
 */
public class AppConfig {

	public static void main(String[] args) {
		try {
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

			// Example with a custom Store
			// cacheConfig.setCacheStoreFactory(FactoryBuilder.<StudentStore>factoryOf(StudentStore.class));

			cacheConfig.setName("StudentCache");
			cacheConfig.setAtomicityMode(CacheAtomicityMode.ATOMIC);
			cacheConfig.setBackups(0);

			// https://codeahoy.com/2017/08/11/caching-strategies-and-how-to-choose-the-right-one/
			// Write-Back = Write Behind: Write on cache, after delay, write on DB
			cacheConfig.setWriteBehindEnabled(true);
			// https://apacheignite.readme.io/docs/3rd-party-store
			// Size in memory before flush to DB
			// cacheConfig.setWriteBehindFlushSize(256);
			// Time before flush on DB
			cacheConfig.setWriteBehindFlushFrequency(250);

			// DS Factory for the Caches
			H2DataSourceFactory dsFactory = H2DataSourceFactory.getInstance();

			// Add classes with ignite notation to process
			IgniteAutoConfig.addClass(Student.class);

			CacheJdbcPojoStoreFactory<Object, Object> storeFactory = new CacheJdbcPojoStoreFactory<>();
			storeFactory.setDataSourceFactory(dsFactory);
			storeFactory.setDialect(new H2Dialect());
			storeFactory.setTypes(IgniteAutoConfig.getJDBCTypes());

			cacheConfig.setCacheStoreFactory(storeFactory);

			// Ignite notations over Apache Ignite
			cacheConfig.setQueryEntities(IgniteAutoConfig.getQueryEntities());

			igniteConfiguration.setCacheConfiguration(cacheConfig);

			Ignite ignite = Ignition.start(igniteConfiguration);
			System.out.println("[IgniteServerNode] Node started");
			IgniteCache<Long, Student> cache = ignite.getOrCreateCache("StudentCache");
			cache.loadCache(null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}