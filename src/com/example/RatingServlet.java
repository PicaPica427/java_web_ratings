package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/", "/index", "/ratings", "/ratings/submit"})
public class RatingServlet extends HttpServlet {
    private Connection conn;
    private RatingRepository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        conn = (Connection) getServletContext().getAttribute("DB_CONNECTION");
        if (conn == null) {
            throw new ServletException("Database connection not found in servlet context");
        }
        repository = new RatingRepository(conn);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // 获取所有评分数据
            List<Rating> ratings = repository.findAll();
            request.setAttribute("ratings", ratings);
            
            // 检查请求路径，如果是/或/index也处理
            String path = request.getServletPath();
            if ("/".equals(path) || "/index".equals(path) || "/ratings".equals(path) || "/ratings/submit".equals(path)) {
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "无法获取评分数据，请稍后再试");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String courseId = request.getParameter("courseId");
        String ratingParam = request.getParameter("score");
        String comment = request.getParameter("comment");

        if (courseId == null || ratingParam == null || comment == null || 
            courseId.isEmpty() || comment.isEmpty()) {
            request.setAttribute("errorMessage", "无效输入: 请填写所有字段");
            doGet(request, response);
            return;
        }

        try {
            int score = Integer.parseInt(ratingParam);
            int courseid = Integer.parseInt(courseId);
            if (score < 1 || score > 100) {
                request.setAttribute("errorMessage", "无效评分: 评分必须在1到100之间");
                doGet(request, response);
                return;
            }
            
            Rating newRating = new Rating(courseid, score, comment);
            repository.save(newRating);
            response.sendRedirect(request.getContextPath() + "/ratings");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "无效评分: 请输入有效的数字");
            doGet(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "保存评分时出错，请稍后再试");
            doGet(request, response);
        }
    }
}
//
//// RatingServlet.java
//package com.example;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//
//@WebServlet(urlPatterns = {"/", "/index", "/ratings", "/ratings/submit"})
//public class RatingServlet extends HttpServlet {
//	
//    private Connection conn;
//
//    @Override
//    public void init() throws ServletException {
//        super.init();
//        conn = (Connection) getServletContext().getAttribute("DB_CONNECTION");
//        if (conn == null) {
//            throw new ServletException("Database connection not found in servlet context");
//        }
//        System.out.println("aaaaaaaaaaaaaaaa：：："+ conn);
//        repository =  new RatingRepository(conn);
//    }
//	
////    private final RatingRepository repository = new RatingRepository(conn);
//    private  RatingRepository repository;
//
//    // 显示评分列表 (GET /ratings)
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        
//        List<Rating> ratings = null;
//        System.out.println("repository：：："+ repository);
//		try {
//			ratings = repository.findAll();
//			for (Rating a: ratings) {
//				System.out.println(a.toString());
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        request.setAttribute("ratings", ratings);
//        request.getRequestDispatcher("/index.jsp").forward(request, response); // 直接转发到 index.jsp
//    }
//
//    // 保存评分 (POST /ratings/submit)  注意这里的URL是 /ratings/submit
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//
//        String courseId = request.getParameter("courseId");
//        String ratingParam = request.getParameter("score");
//        String comment = request.getParameter("comment");
//
//        if (courseId == null || ratingParam == null || comment == null || courseId.isEmpty() || comment.isEmpty()) {
//            request.setAttribute("errorMessage", "Invalid input: Please fill in all fields.");
//            doGet(request, response); // 显示错误信息，并重新显示表单
//            return;
//        }
//
//        try {
//            int score = Integer.parseInt(ratingParam);     //注意类型转换
//            int courseid = Integer.parseInt(courseId);
//            if (score < 1 || score > 5) {
//                request.setAttribute("errorMessage", "Invalid rating: Rating must be between 1 and 5.");
//                doGet(request, response); // 显示错误信息，并重新显示表单
//                return;
//            }
//            Rating newRating = new Rating(courseid, score, comment);
//            System.out.println("111111111111111111111111");
//            repository.save(newRating);
//            System.out.println("222222222222222222222222");
//            response.sendRedirect(request.getContextPath() + "/ratings"); // 重定向到评分列表
//            System.out.println("3333333333333333333333333");
//        } catch (NumberFormatException e) {
//            request.setAttribute("errorMessage", "Invalid rating: Please enter a valid number.");
//            doGet(request, response); // 显示错误信息，并重新显示表单
//        } catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//}
////Controller
////RatingServlet 类是一个 Servlet，它是 Web 应用的 控制器 (Controller)。 
////它负责接收 HTTP 请求，处理业务逻辑（通常是通过调用 RatingRepository），并将结果呈现给用户（通常是通过转发到 JSP 页面）。
////@WebServlet 注解: 将这个 Servlet 映射到 /ratings 和 /ratings/submit 这两个 URL。
////repository 字段: 通过 new RatingRepository() 创建了一个 RatingRepository 的实例。 这是一种简单的 依赖注入 方式（更高级的方式是使用 Spring 框架的依赖注入）。
////doGet() 方法: 处理 GET 请求（显示评分列表）。
////repository.findAll(): 从 RatingRepository 获取所有评分。
////request.setAttribute("ratings", ratings): 将评分列表存储到 request 对象的属性中，以便 JSP 页面可以访问。
////request.getRequestDispatcher("index.jsp").forward(request, response): 将请求转发到 index.jsp 页面进行渲染。
////doPost() 方法: 处理 POST 请求（保存新评分）。
////request.getParameter(): 从请求中获取表单参数。
////输入验证: 检查输入是否为空，评分是否在有效范围内。
////Integer.parseInt(): 将评分参数从字符串转换为整数。
////new Rating(...): 创建一个新的 Rating 对象。
////repository.save(newRating): 将新评分保存到 RatingRepository。
////response.sendRedirect(...): 重定向到 /ratings，以便用户可以看到更新后的评分列表。
////异常处理: 使用 try-catch 块捕获 NumberFormatException (如果评分参数不是有效的数字
