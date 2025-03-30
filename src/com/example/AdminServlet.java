package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() throws ServletException {
        super.init();
        conn = (Connection) getServletContext().getAttribute("DB_CONNECTION");
        if (conn == null) {
            throw new ServletException("Database connection not found in servlet context");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  // 前端请求共同入口
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.print(request.getRequestURI()); // /infoSubSys/admin

        String table = request.getParameter("table");
        String action = request.getParameter("action");
        try {
            if (table == null || table.isEmpty()) {
                // 显示管理主页面
                request.getRequestDispatcher("/admin.jsp").forward(request, response);
            } else if ("list".equals(action)) {
                // 显示特定表格数据
                System.out.println(table+ " " + action);
                handleList(request, response, table);
            }
//            } else if ("edit".equals(action)) {
//                // 显示编辑表单
//                handleEditForm(request, response, table);
//            }
        } catch (SQLException e) {
            throw new ServletException("Database operation failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String table = request.getParameter("table");
        String action = request.getParameter("action");

        if (table == null || table.isEmpty() || action == null || action.isEmpty()) {
            throw new ServletException("Missing table or action parameter");
        }

        try {
            switch (action) {
                case "add":
                    handleAdd(request, table);
                    break;
                case "delete":
                    handleDelete(request, table);
                    break;
                case "update":
                    handleUpdate(request, table);
                    break;
                default:
                    throw new ServletException("Invalid action: " + action);
            }
            response.sendRedirect(request.getContextPath() + "/admin?table=" + table + "&action=list");
//            request.getRequestDispatcher("/admin.jsp").forward(request, response);
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Database operation failed", e);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response, String table) 
            throws SQLException, ServletException, IOException {
    	System.out.println("table" + table);
        switch (table) {
            case "teachers":
                TeacherRepository teacherRepo = new TeacherRepository(conn);
                request.setAttribute("items", teacherRepo.findAll());
//                System.out.println("items 设置完成");
                break;
            case "courses":
                CourseRepository courseRepo = new CourseRepository(conn);
                request.setAttribute("items", courseRepo.findAll());
                break;
            case "ratings":
                RatingRepository ratingRepo = new RatingRepository(conn);
                request.setAttribute("items", ratingRepo.findAll());
                break;
            default:
                throw new ServletException("Unknown table: " + table);
        }
        request.setAttribute("currentTable", table);
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

   

    private void handleAdd(HttpServletRequest request, String table) throws SQLException {
        switch (table) {
            case "teachers":
                new TeacherRepository(conn).add(
                    new Teacher(
                        ParamUtils.getIntParameter(request, "id", 0),
                        ParamUtils.getRequiredString(request, "name", "教师姓名不能为空")
                    ));
                break;
                
            case "courses":
                new CourseRepository(conn).add(
                    new Course(
                        ParamUtils.getIntParameter(request, "id", 0),
                        ParamUtils.getRequiredString(request, "courseName", "课程名称不能为空"),
                        ParamUtils.getRequiredInt(request, "teacherId", "教师ID不能为空且必须是数字")
                    ));
                break;
                
            case "ratings":
                new RatingRepository(conn).add(
                    new Rating(
                        ParamUtils.getIntParameter(request, "id", 0),
                        ParamUtils.getRequiredInt(request, "courseId", "课程ID不能为空且必须是数字"),
                        ParamUtils.getRequiredInt(request, "score", "评分不能为空且必须是1-5的数字"),
                        ParamUtils.getRequiredString(request, "comment", "评价内容不能为空")
                    ));
                break;
                
            default:
                throw new IllegalArgumentException("未知的表类型: " + table);
        }
    }
    private void handleDelete(HttpServletRequest request, String table) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        switch (table) {
            case "teachers":
                TeacherRepository teacherRepo = new TeacherRepository(conn);
                teacherRepo.delete(id);
                break;
            case "courses":
                CourseRepository courseRepo = new CourseRepository(conn);
                courseRepo.delete(id);
                break;
            case "ratings":
                RatingRepository ratingRepo = new RatingRepository(conn);
                ratingRepo.delete(id);
                break;
        }
    }

    private void handleUpdate(HttpServletRequest request, String table) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        switch (table) {
            case "teachers":
                TeacherRepository teacherRepo = new TeacherRepository(conn);
                String teacherName = request.getParameter("name");
                teacherRepo.update(new Teacher(id, teacherName));
                break;
            case "courses":
                CourseRepository courseRepo = new CourseRepository(conn);
                String courseName = request.getParameter("courseName");
                int teacherId = Integer.parseInt(request.getParameter("teacherId"));
                courseRepo.update(new Course(id, courseName, teacherId));
                break;
            case "ratings":
                RatingRepository ratingRepo = new RatingRepository(conn);
                int courseId = Integer.parseInt(request.getParameter("courseId"));
                int score = Integer.parseInt(request.getParameter("score"));
                String comment = request.getParameter("comment");
                ratingRepo.update(new Rating(id, courseId, score, comment));
                break;
        }
    }
}

class ParamUtils {
    public static int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
        String paramValue = request.getParameter(paramName);
        if (paramValue == null || paramValue.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(paramValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int getRequiredInt(HttpServletRequest request, String paramName, String errorMsg) 
        throws IllegalArgumentException {
        String paramValue = request.getParameter(paramName);
        if (paramValue == null || paramValue.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }
        try {
            return Integer.parseInt(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static String getRequiredString(HttpServletRequest request, String paramName, String errorMsg) 
        throws IllegalArgumentException {
        String value = request.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }
        return value.trim();
    }
}