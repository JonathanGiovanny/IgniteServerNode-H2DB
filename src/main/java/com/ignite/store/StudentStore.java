package com.ignite.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;

import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

import org.apache.ignite.cache.store.CacheStore;
import org.apache.ignite.lang.IgniteBiInClosure;

import com.ignite.model.Student;
import com.ignite.persistence.H2DataSourceFactory;

/**
 * https://apacheignite.readme.io/v1.9/docs/persistent-store
 * @author jcamargos
 *
 */
public class StudentStore implements CacheStore<Long, Student> {

	private Connection conn = H2DataSourceFactory.getInstance().getConnection();

	@Override
	public void loadCache(IgniteBiInClosure<Long, Student> clo, Object... args) throws CacheLoaderException {
		LocalTime startTime = LocalTime.now();
		System.out.println("[loadCache] Loading cache from the DB");

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM STUDENT");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Student person = new Student(rs.getLong(1), rs.getString(2), rs.getDouble(3));
				clo.apply(person.getId(), person);
			}
		
			LocalTime endTime = LocalTime.now();
			System.out.println("[loadCache] Time pre: " + startTime);
			System.out.println("[loadCache] Time pos: " + endTime);
			System.out.println("[loadCache] Time diff: " + (endTime.getNano() - startTime.getNano()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is called whenever "get(...)" methods are called on IgniteCache.
	 */
	@Override
	public Student load(Long key) throws CacheLoaderException {
		System.out.println("[load] Loading single row from DB");

		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM STUDENT WHERE id = ?");
			ps.setString(1, key.toString());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new Student(rs.getLong(1), rs.getString(2), rs.getDouble(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<Long, Student> loadAll(Iterable<? extends Long> arg0) throws CacheLoaderException {
		return null;
	}

	@Override
	public void delete(Object arg0) throws CacheWriterException {

	}

	@Override
	public void deleteAll(Collection<?> arg0) throws CacheWriterException {

	}

	@Override
	public void write(Entry<? extends Long, ? extends Student> arg0) throws CacheWriterException {

	}

	@Override
	public void writeAll(Collection<Entry<? extends Long, ? extends Student>> arg0) throws CacheWriterException {

	}

	@Override
	public void sessionEnd(boolean commit) throws CacheWriterException {

	}
}
