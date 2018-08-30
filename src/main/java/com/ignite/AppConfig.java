package com.ignite;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import com.ignite.model.Student;

/**
 * https://apacheignite.readme.io/docs/getting-started
 * https://dzone.com/articles/in-memory-data-grid-with-apache-ignite
 * 
 * * Details of previous configurations on the IgniteServerNode project on my github
 * 
 * @author jcamargos
 *
 */
public class AppConfig {

	public static void main(String[] args) {
		IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
		igniteConfiguration.setClientMode(false);

		CacheConfiguration<Long, Student> cacheConfig = new CacheConfiguration<>();
		
		/** https://apacheignite.readme.io/docs/3rd-party-store#section-read-through-and-write-through */
		// Read data not found in a cache from the database
		cacheConfig.setReadThrough(true);
		// Enable export cache updates to the database
		cacheConfig.setWriteThrough(true);
		
		cacheConfig.setName("StudentCache");
		cacheConfig.setAtomicityMode(CacheAtomicityMode.ATOMIC);
		cacheConfig.setBackups(1);

		igniteConfiguration.setCacheConfiguration(cacheConfig);

		TcpDiscoverySpi spi = new TcpDiscoverySpi();
		TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		ipFinder.setMulticastGroup("228.10.10.157");
		spi.setIpFinder(ipFinder);

		igniteConfiguration.setDiscoverySpi(spi);

		Ignition.start(igniteConfiguration);
	}

}