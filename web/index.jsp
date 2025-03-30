<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>课程评分系统</title>
    <link href="https://fastly.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1 class="text-center mb-4">课程评分列表</h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/ratings/submit" method="POST">
        <div class="mb-3">
            <label class="form-label">课程编号</label>
            <input type="number" name="courseId" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">评分（1-100）</label>
            <input type="number" name="score" class="form-control" min="1" max="100" required>
        </div>
        <div class="mb-3">
            <label class="form-label">评价</label>
            <textarea name="comment" class="form-control" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">提交</button>
    </form>

    <table class="table mt-4">
        <thead>
            <tr>
                <th>课程名称</th>
                <th>教师</th>
                <th>评分</th>
                <th>评价</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${ratings}" var="rating">
                <tr>
                    <td>${rating.courseName}</td>
                    <td>${rating.teacherName}</td>
                    <td>${rating.score}</td>
                    <td>${rating.comment}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <div class="admin-link">
        <a href="${pageContext.request.contextPath}/admin?table=ratings&action=list">进入数据管理界面</a>
    </div>
</div>

</body>
</html>
