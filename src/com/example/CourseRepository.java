package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {
	private final Connection conn;

	public CourseRepository(Connection conn) {
		this.conn = conn;
	}

	// 查询所有教师
	public List<Course> findAll() throws SQLException {
		List<Course> courses = new ArrayList<>();
//		String sql = "SELECT courses.course_id, courses.course_name, teachers.name FROM courses INNER JOIN teachers ON courses.teacher_id = teachers.teacher_id;";
		String sql = "SELECT courses.course_id, courses.course_name, courses.teacher_id FROM courses";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				courses.add(new Course(rs.getInt("course_id"), rs.getString("course_name"), rs.getInt("teacher_id")));
			}
		}
		return courses;
	}

	// 添加教师            //TODO:
	
	public void add(Course course) throws SQLException {
	    if (course == null) {
	        throw new IllegalArgumentException("Course cannot be null");
	    }
	    
	    String sql;
	    if (course.getCourseId() == 0) {
	        sql = "INSERT INTO courses (course_name, teacher_id) VALUES (?, ?)";
	    } else {
	        sql = "INSERT INTO courses (course_id, course_name, teacher_id) VALUES (?, ?, ?)";
	    }
	    
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        int paramIndex = 1;
	        if (course.getCourseId() != 0) {
	            stmt.setInt(paramIndex++, course.getCourseId());
	        }
	        stmt.setString(paramIndex++, course.getCourseName());
	        stmt.setInt(paramIndex, course.getTeacherId());
	        
	        stmt.executeUpdate();
	    }
	}
	
	public Course findById(int id) throws SQLException {
	    String sql = "SELECT * FROM courses WHERE course_id = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return new Course(rs.getInt("course_id"),rs.getString("course_name"), rs.getInt("teacher_id"));
	        }
	    }
	    return null;
	}

	 

	/*
	 * public void add(Teacher teacher) throws SQLException { String sql =
	 * "INSERT INTO teachers (name) VALUES (?)"; try (PreparedStatement stmt =
	 * conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	 * stmt.setString(1, teacher.getName()); stmt.executeUpdate();
	 * 
	 * // 获取自增ID try (ResultSet generatedKeys = stmt.getGeneratedKeys()) { if
	 * (generatedKeys.next()) { teacher.setId(generatedKeys.getInt(1)); } } } }
	 */

	// 删除教师
	public void delete(int id) throws SQLException {
	    System.out.println("Attempting to delete course with ID: " + id);
		String sql = "DELETE FROM courses WHERE course_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	// 更新教师信息
	public void update(Course course) throws SQLException {
		String sql = "UPDATE courses "+
				"SET course_name = ?, teacher_id = ? " + 
						"WHERE course_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, course.getCourseName());
			stmt.setInt(2, course.getTeacherId());
			stmt.setInt(3, course.getCourseId());
			stmt.executeUpdate();
		}
	}
}
