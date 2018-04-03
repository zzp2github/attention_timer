package com.hexun.attention.db.mongo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.hexun.attention.db.mongo.TeacherDao;
import com.hexun.attention.domain.entity.Teacher;

/**
 * 描述：TeacherInfo mongodb操作
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年7月18日
 * @version v1.0
 */
@Repository
public class TeacherDaoImpl implements TeacherDao {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * {@inheritDoc}
	 */
	public List<Teacher> getAllTeacher() {
		return mongoTemplate.findAll(Teacher.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Teacher> getTeachers(int offset, int limit) {
		Query query = new Query();
		query.skip(offset).limit(limit);
		query.with(new Sort(Direction.DESC, "_id"));
		return mongoTemplate.find(query, Teacher.class);
	}

}
