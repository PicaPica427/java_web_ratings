// Rating.java
package com.example;

public class Rating {
	private int id;         // ratings.rating_id
	private int courseId;	// courses.course_id
	private int score;		// ratings.score
	private String comment;	// ratings.comment
    private String courseName;  // JOIN courses
    private String teacherName;	// JOIN teachers
    
    
    public Rating(int id, int courseId, int score, String comment) {
		this.id = id;
		this.courseId = courseId;
		this.score = score;
		this.comment = comment;
	}
    
    public Rating(int courseId, int score, String comment) {
		this.courseId = courseId;
		this.score = score;
		this.comment = comment;
	}
    
    

	public Rating(int id, int courseId, int score, String comment, String courseName, String teacherName) {
		this.id = id;
		this.courseId = courseId;
		this.score = score;
		this.comment = comment;
		this.courseName = courseName;
		this.teacherName = teacherName;
		System.out.println(this.toString());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	@Override
	public String toString() {
		return "Rating [id=" + id + ", courseId=" + courseId + ", score=" + score + ", comment=" + comment
				+ ", courseName=" + courseName + ", teacherName=" + teacherName + "]";
	}
    
    

}
//Model
//Rating 类是一个 数据模型 或 实体类 (Entity Class)，它代表了一条课程评分记录。 它定义了评分记录应包含的数据字段
//关键操作:
//定义数据字段: courseName (课程名称), rating (评分), comment (评论)。
//构造函数: Rating(String courseName, int rating, String comment) 用于创建 Rating 对象。
//Getter 方法: getCourseName(), getRating(), getComment() 用于获取对象的属性值。 
//这些 getter 方法对于 JSP 页面中通过 EL 表达式 (${rating.courseName}) 访问属性值非常重要。