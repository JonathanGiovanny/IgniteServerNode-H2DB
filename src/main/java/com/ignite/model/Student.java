package com.ignite.model;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QueryGroupIndex;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * SQLQuery to create this table:
 * CREATE TABLE STUDENT (
 * 		STUDENT_ID 		BIGINT PRIMARY KEY,
 * 		STUDENT_NAME	VARCHAR,
 * 		STUDENT_AVG		DOUBLE
 * );
 * CREATE INDEX STUDENT_IDX ON STUDENT (STUDENT_ID);
 * @author jcamargos
 */
@Accessors(chain = true)
@Data    
@NoArgsConstructor
@AllArgsConstructor
@QueryGroupIndex.List(@QueryGroupIndex(name = "idx1"))
public class Student implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8833325000869266534L;

	@QuerySqlField(index = true, orderedGroups = { @QuerySqlField.Group(name = "idx1", order = 0) })
	private Long id;
	@QuerySqlField(index = true, orderedGroups = { @QuerySqlField.Group(name = "idx1", order = 1) })
	private String name;
	private Double avg;
}
