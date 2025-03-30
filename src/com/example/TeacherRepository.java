package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository {
	private final Connection conn;

	public TeacherRepository(Connection conn) {
		this.conn = conn;
	}

	// 查询所有教师
	public List<Teacher> findAll() throws SQLException {
		List<Teacher> teachers = new ArrayList<>();
		String sql = "SELECT teacher_id, name FROM teachers";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				teachers.add(new Teacher(rs.getInt("teacher_id"), rs.getString("name")));
			}
		}
		return teachers;
	}

	// 添加教师
	
	public void add(Teacher teacher) throws SQLException {
	    if (teacher == null || teacher.getName() == null) {
	        throw new IllegalArgumentException("Teacher or teacher's name cannot be null");
	    }
	    
	    String sql = "INSERT INTO teachers (name) VALUES (?)";
	    if (teacher.getId() != 0) {
	        sql = "INSERT INTO teachers (teacher_id, name) VALUES (?, ?)";
	    }
	    
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        int paramIndex = 1;
	        if (teacher.getId() != 0) {
	            stmt.setInt(paramIndex++, teacher.getId());
	        }
	        stmt.setString(paramIndex, teacher.getName());
	        stmt.executeUpdate();
	    }
	}
	
	public Teacher findById(int id) throws SQLException {
	    String sql = "SELECT * FROM teachers WHERE teacher_id = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return new Teacher(rs.getInt("teacher_id"), rs.getString("name"));
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
	    System.out.println("Attempting to delete teacher with ID: " + id);
		String sql = "DELETE FROM teachers WHERE teacher_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	// 更新教师信息
	public void update(Teacher teacher) throws SQLException {
		String sql = "UPDATE teachers SET name = ? WHERE teacher_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, teacher.getName());
			stmt.setInt(2, teacher.getId());
			stmt.executeUpdate();
		}
	}
}
