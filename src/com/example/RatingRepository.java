package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class RatingRepository{
	private final Connection conn;
	
//  private List<Rating> ratings = new ArrayList<>();
  

  

  public void save(Rating rating) throws SQLException {
//      rating.setTeacherName("RatingRepository测试2");
//      rating.setCourseName("RatingRepository测试1");
//  		ratings.add(rating);
      	this.add(rating);
  }

	

	public RatingRepository(Connection conn) {
		this.conn = conn;
//		System.out.println("RatingRepository111:" + conn);
//		System.out.println("RatingRepository" + this.conn);
	}

	// 查询所有教师
	public List<Rating> findAll() throws SQLException {
		List<Rating> ratings = new ArrayList<>();
		System.out.println("didididi");
//		String sql = "SELECT t.name AS teacher_name, c.course_name, r.score, r.comment FROM teachers t JOIN courses c ON t.teacher_id = c.teacher_id LEFT JOIN ratings r ON c.course_id = r.courses_course_id ORDER BY t.teacher_id, c.course_id, r.rating_id";
		String sql = "SELECT DISTINCT t.teacher_id, t.name AS teacher_name, c.course_id, c.course_name, r.rating_id, r.score, r.comment FROM teachers t JOIN courses c ON t.teacher_id = c.teacher_id  JOIN ratings r ON c.course_id = r.course_id ORDER BY t.teacher_id, c.course_id, r.rating_id";
		System.out.println("conn=" + conn);
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			System.out.println(conn);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
			    ratings.add(new Rating(
			            rs.getInt("rating_id"),      // 去掉了 "r."
			            rs.getInt("course_id"),      // 去掉了 "c." 
			            rs.getInt("score"),          // 去掉了 "r."
			            rs.getString("comment"),     // 去掉了 "r." 
			            rs.getString("course_name"), // 去掉了 "c."
			            rs.getString("teacher_name") // 直接使用别名（原SQL中已定义）
			        ));
//				ratings.add(new Rating(rs.getInt("r.rating_id"), rs.getInt("c.course_id"), rs.getInt("r.score"),  rs.getString("r.comment")            ));
			}
		}
		for( Rating a:ratings) {
			System.out.println(a.toString());
		}
		return ratings;
	}


	
	public void add(Rating rating) throws SQLException {
	    if (rating == null) {
	        throw new IllegalArgumentException("Rating cannot be null");
	    }
	    
	    String sql;
	    if (rating.getId() == 0) {
	        sql = "INSERT INTO ratings (course_id, score, comment) VALUES (?, ?, ?)";
	    } else {
	        sql = "INSERT INTO ratings (rating_id, course_id, score, comment) VALUES (?, ?, ?, ?)";
	    }
	    
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        int paramIndex = 1;
	        if (rating.getId() != 0) {
	            stmt.setInt(paramIndex++, rating.getId());
	        }
	        stmt.setInt(paramIndex++, rating.getCourseId());
	        stmt.setInt(paramIndex++, rating.getScore());
	        stmt.setString(paramIndex, rating.getComment());
	        
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
	    System.out.println("Attempting to delete rating with ID: " + id);
		String sql = "DELETE FROM ratings WHERE rating_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	// 更新教师信息
	public void update(Rating rating) throws SQLException {
	    String sql = "UPDATE ratings SET course_id = ?, score = ?, comment = ? WHERE rating_id = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, rating.getCourseId());
	        stmt.setInt(2, rating.getScore());
	        stmt.setString(3, rating.getComment());
	        stmt.setInt(4, rating.getId());  // 设置 WHERE 条件中的 rating_id
	        stmt.executeUpdate();
	    }
	}
}




//// RatingRepository.java (内存实现，仅供演示)
//package com.example;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RatingRepository {
//    private List<Rating> ratings = new ArrayList<>();
//
//    public void save(Rating rating) {
//        rating.setTeacherName("RatingRepository测试2");
//        rating.setCourseName("RatingRepository测试1");
//    	ratings.add(rating);
//        
//    }
//
//    public List<Rating> findAll() {
//        return new ArrayList<>(ratings); // 返回副本，防止外部修改
//    }
//}
////RatingRepository 类是一个 数据访问对象 (DAO)。 它的职责是 隔离 数据访问逻辑 (例如，从数据库读取数据、将数据保存到数据库)。 
////这使得 RatingServlet 不需要直接与数据库交互，提高了代码的可维护性和可测试性。
//
////注意: 这个版本是一个 内存实现，它使用一个 List 来存储评分。 这意味着数据只在应用程序运行期间存在，
////重启后会丢失。 在实际应用中，你需要将其替换为数据库实现
//
////关键操作:
////ratings 字段: 一个 List<Rating>，用于在内存中存储评分对象。
////ve(Rating rating) 方法: 将一个新的 Rating 对象添加到 ratings 列表中。
////findAll() 方法: 返回 ratings 列表的一个 副本。 返回副本是为了防止外部代码直接修改 ratings 列表，从而破坏封装性。