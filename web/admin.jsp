<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>课程评分系统 - 管理主页</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fastly.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .management-container { max-width: 800px; margin: 50px auto; padding: 20px; }
        .btn-management { min-width: 150px; height: 50px; font-size: 1.2rem; }
        /* 表单样式 */
        #addTeacherForm, #updateTeacherForm, 
        #addCourseForm, #updateCourseForm,
        #addRatingForm, #updateRatingForm { 
            display: none;
            background-color: #f8f9fa; 
            padding: 15px; 
            border-radius: 5px; 
            margin-bottom: 20px;
        }
        .btn-warning.text-white {
            color: white !important;
        }
    </style>
</head>
<body class="bg-light">
    <div class="management-container bg-white rounded shadow-sm p-4">
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger mb-4">${errorMessage}</div>
        </c:if>

        <h1 class="text-center mb-4">管理系统</h1>
        
        <div class="row g-3">
            <div class="col-md-4">
                <form method="get" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="table" value="courses">
                    <input type="hidden" name="action" value="list">
                    <button type="submit" class="btn bg-danger btn-management w-100">
                        课程管理
                    </button>
                </form>
            </div>
            
            <div class="col-md-4">
                <form method="get" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="table" value="teachers">
                    <input type="hidden" name="action" value="list">
                    <button type="submit" class="btn btn-success btn-management w-100">
                        老师管理
                    </button>
                </form>
            </div>
            
            <div class="col-md-4">
                <form method="get" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="table" value="ratings">
                    <input type="hidden" name="action" value="list">
                    <button type="submit" class="btn btn-info btn-management w-100">
                        评分管理
                    </button>
                </form>
            </div>
        </div>
        
        <c:if test="${not empty currentTable}">
            <c:if test="${currentTable == 'teachers'}">
                <!-- 教师管理操作按钮 -->
                <div class="mb-3 d-flex gap-2" style="margin-top: 20px">
                    <button id="showAddTeacherBtn" class="btn btn-primary">新增教师</button>
                    <button id="showUpdateTeacherBtn" class="btn btn-warning text-white">修改教师信息</button>
                </div>
                
                <!-- 新增教师表单 -->
                <div id="addTeacherForm">
                    <h5>添加新教师</h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin" class="row g-3">
                        <input type="hidden" name="table" value="teachers">
                        <input type="hidden" name="action" value="add">
                        
                        <div class="col-md-6">
                            <label for="teacherName" class="form-label">教师姓名</label>
                            <input type="text" class="form-control" id="teacherName" name="name" required>
                        </div>
                        
                        <div class="col-md-6">
                            <label for="teacherId" class="form-label">教师ID（可选）</label>
                            <input type="text" class="form-control" id="teacherId" name="id">
                        </div>
                        
                        <div class="col-12">
                            <button type="submit" class="btn btn-success">保存</button>
                            <button type="button" id="cancelAddTeacherBtn" class="btn btn-secondary">取消</button>
                        </div>
                    </form>
                </div>
                
                <!-- 修改教师表单 -->
                <div id="updateTeacherForm">
                    <h5>修改教师信息</h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin" class="row g-3">
                        <input type="hidden" name="table" value="teachers">
                        <input type="hidden" name="action" value="update">
                        
                        <div class="col-md-6">
                            <label for="updateTeacherId" class="form-label">教师ID</label>
                            <input type="text" class="form-control" id="updateTeacherId" name="id" required>
                        </div>
                        
                        <div class="col-md-6">
                            <label for="updateTeacherName" class="form-label">新教师姓名</label>
                            <input type="text" class="form-control" id="updateTeacherName" name="name" required>
                        </div>
                        
                        <div class="col-12">
                            <button type="submit" class="btn btn-success">更新</button>
                            <button type="button" id="cancelUpdateTeacherBtn" class="btn btn-secondary">取消</button>
                        </div>
                    </form>
                </div>
            </c:if>
            
            <c:if test="${currentTable == 'courses'}">
                <!-- 课程管理操作按钮 -->
                <div class="mb-3 d-flex gap-2" style="margin-top: 20px">
                    <button id="showAddCourseBtn" class="btn btn-primary">新增课程</button>
                    <button id="showUpdateCourseBtn" class="btn btn-warning text-white">修改课程信息</button>
                </div>
                
                <!-- 新增课程表单 -->
                <div id="addCourseForm">
                    <h5>添加新课程</h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin" class="row g-3">
                        <input type="hidden" name="table" value="courses">
                        <input type="hidden" name="action" value="add">
                        
                        <div class="col-md-4">
                            <label for="courseId" class="form-label">课程ID</label>
                            <input type="text" class="form-control" id="courseId" name="id">
                        </div>
                        
                        <div class="col-md-4">
                            <label for="courseName" class="form-label">课程名称</label>
                            <input type="text" class="form-control" id="courseName" name="courseName" required>
                        </div>
                        
                        <div class="col-md-4">
                            <label for="courseTeacherId" class="form-label">教师ID</label>
                            <input type="text" class="form-control" id="courseTeacherId" name="teacherId" required>
                        </div>
                        
                        <div class="col-12">
                            <button type="submit" class="btn btn-success">保存</button>
                            <button type="button" id="cancelAddCourseBtn" class="btn btn-secondary">取消</button>
                        </div>
                    </form>
                </div>
                
                <!-- 修改课程表单 -->
                <div id="updateCourseForm">
                    <h5>修改课程信息</h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin" class="row g-3">
                        <input type="hidden" name="table" value="courses">
                        <input type="hidden" name="action" value="update">
                        
                        <div class="col-md-4">
                            <label for="updateCourseId" class="form-label">课程ID</label>
                            <input type="text" class="form-control" id="updateCourseId" name="id" required>
                        </div>
                        
                        <div class="col-md-4">
                            <label for="updateCourseName" class="form-label">课程名称</label>
                            <input type="text" class="form-control" id="updateCourseName" name="courseName" required>
                        </div>
                        
                        <div class="col-md-4">
                            <label for="updateCourseTeacherId" class="form-label">教师ID</label>
                            <input type="text" class="form-control" id="updateCourseTeacherId" name="teacherId" required>
                        </div>
                        
                        <div class="col-12">
                            <button type="submit" class="btn btn-success">更新</button>
                            <button type="button" id="cancelUpdateCourseBtn" class="btn btn-secondary">取消</button>
                        </div>
                    </form>
                </div>
            </c:if>
            
            <c:if test="${currentTable == 'ratings'}">
                <!-- 评分管理操作按钮 -->
                <div class="mb-3 d-flex gap-2" style="margin-top: 20px">
                    <button id="showAddRatingBtn" class="btn btn-primary">新增评分</button>
                    <button id="showUpdateRatingBtn" class="btn btn-warning text-white">修改评分信息</button>
                </div>
                
                <!-- 新增评分表单 -->
                <div id="addRatingForm">
                    <h5>添加新评分</h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin" class="row g-3">
                        <input type="hidden" name="table" value="ratings">
                        <input type="hidden" name="action" value="add">
                        
                        <div class="col-md-3">
                            <label for="ratingId" class="form-label">评分ID（可选）</label>
                            <input type="text" class="form-control" id="ratingId" name="id">
                        </div>
                        
                        <div class="col-md-3">
                            <label for="ratingCourseId" class="form-label">课程ID</label>
                            <input type="text" class="form-control" id="ratingCourseId" name="courseId" required>
                        </div>
                        
                        <div class="col-md-3">
                            <label for="ratingScore" class="form-label">评分（1-100）</label>
                            <input type="number" class="form-control" id="ratingScore" name="score" min="1" max="100" required>
                        </div>
                        
                        <div class="col-md-3">
                            <label for="ratingComment" class="form-label">评价</label>
                            <input type="text" class="form-control" id="ratingComment" name="comment" required>
                        </div>
                        
                        <div class="col-12">
                            <button type="submit" class="btn btn-success">保存</button>
                            <button type="button" id="cancelAddRatingBtn" class="btn btn-secondary">取消</button>
                        </div>
                    </form>
                </div>
                
                <!-- 修改评分表单 -->
                <div id="updateRatingForm">
                    <h5>修改评分信息</h5>
                    <form method="post" action="${pageContext.request.contextPath}/admin" class="row g-3">
                        <input type="hidden" name="table" value="ratings">
                        <input type="hidden" name="action" value="update">
                        
                        <div class="col-md-3">
                            <label for="updateRatingId" class="form-label">评分ID</label>
                            <input type="text" class="form-control" id="updateRatingId" name="id" required>
                        </div>
                        
                        <div class="col-md-3">
                            <label for="updateRatingCourseId" class="form-label">课程ID</label>
                            <input type="text" class="form-control" id="updateRatingCourseId" name="courseId" required>
                        </div>
                        
                        <div class="col-md-3">
                            <label for="updateRatingScore" class="form-label">评分（1-100）</label>
                            <input type="number" class="form-control" id="updateRatingScore" name="score" min="1" max="100" required>
                        </div>
                        
                        <div class="col-md-3">
                            <label for="updateRatingComment" class="form-label">评价</label>
                            <input type="text" class="form-control" id="updateRatingComment" name="comment" required>
                        </div>
                        
                        <div class="col-12">
                            <button type="submit" class="btn btn-success">更新</button>
                            <button type="button" id="cancelUpdateRatingBtn" class="btn btn-secondary">取消</button>
                        </div>
                    </form>
                </div>
            </c:if>
            
            <c:choose>
                <c:when test="${not empty items}">
                    <table class="table mt-4">
                        <thead>
                            <tr>
                                <c:if test="${currentTable == 'teachers'}">
                                    <th>ID</th>
                                    <th>教师姓名</th>
                                    <th>操作</th>
                                </c:if>
                                <c:if test="${currentTable == 'courses'}">
                                    <th>课程ID</th>
                                    <th>课程名称</th>
                                    <th>教师ID</th>
                                    <th>操作</th>
                                </c:if>
                                <c:if test="${currentTable == 'ratings'}">
                                    <th>评分ID</th>
                                    <th>课程ID</th>
                                    <th>评分</th>
                                    <th>评价</th>
                                    <th>操作</th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${items}" var="item">
                                <tr>
                                    <c:if test="${currentTable == 'teachers'}">
                                        <td>${item.id}</td>
                                        <td>${item.name}</td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/admin" 
                                                  onsubmit="return confirm('确定要删除这位老师吗？');">
                                                <input type="hidden" name="table" value="teachers">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${item.id}">
                                                <button type="submit" class="btn btn-danger btn-sm">
                                                    删除
                                                </button>
                                            </form>
                                        </td>
                                    </c:if>
                                    <c:if test="${currentTable == 'courses'}">
                                        <td>${item.courseId}</td>
                                        <td>${item.courseName}</td>
                                        <td>${item.teacherId}</td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/admin" 
                                                  onsubmit="return confirm('确定要删除这门课程吗？');">
                                                <input type="hidden" name="table" value="courses">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${item.courseId}">
                                                <button type="submit" class="btn btn-danger btn-sm">
                                                    删除
                                                </button>
                                            </form>
                                        </td>
                                    </c:if>
                                    <c:if test="${currentTable == 'ratings'}">
                                        <td>${item.id}</td>
                                        <td>${item.courseId}</td>
                                        <td>${item.score}</td>
                                        <td>${item.comment}</td>
                                        <td>
                                            <form method="post" action="${pageContext.request.contextPath}/admin" 
                                                  onsubmit="return confirm('确定要删除这条评分记录吗？');">
                                                <input type="hidden" name="table" value="ratings">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${item.id}">
                                                <button type="submit" class="btn btn-danger btn-sm">
                                                    删除
                                                </button>
                                            </form>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info mt-4">没有找到相关数据</div>
                </c:otherwise>
            </c:choose>
        </c:if>

        <div class="d-grid gap-2 mt-4">
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                返回首页
            </a>
        </div>
    </div>

    <script src="https://fastly.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // 教师管理表单控制
            setupFormControls(
                'showAddTeacherBtn', 'showUpdateTeacherBtn',
                'addTeacherForm', 'updateTeacherForm',
                'cancelAddTeacherBtn', 'cancelUpdateTeacherBtn'
            );
            
            // 课程管理表单控制
            setupFormControls(
                'showAddCourseBtn', 'showUpdateCourseBtn',
                'addCourseForm', 'updateCourseForm',
                'cancelAddCourseBtn', 'cancelUpdateCourseBtn'
            );
            
            // 评分管理表单控制
            setupFormControls(
                'showAddRatingBtn', 'showUpdateRatingBtn',
                'addRatingForm', 'updateRatingForm',
                'cancelAddRatingBtn', 'cancelUpdateRatingBtn'
            );
            
            function setupFormControls(
                showAddBtnId, showUpdateBtnId,
                addFormId, updateFormId,
                cancelAddBtnId, cancelUpdateBtnId
            ) {
                const showAddBtn = document.getElementById(showAddBtnId);
                const showUpdateBtn = document.getElementById(showUpdateBtnId);
                const addForm = document.getElementById(addFormId);
                const updateForm = document.getElementById(updateFormId);
                const cancelAddBtn = document.getElementById(cancelAddBtnId);
                const cancelUpdateBtn = document.getElementById(cancelUpdateBtnId);
                
                if (showAddBtn && addForm) {
                    showAddBtn.addEventListener('click', function() {
                        addForm.style.display = 'block';
                        updateForm.style.display = 'none';
                        showAddBtn.style.display = 'none';
                        showUpdateBtn.style.display = 'block';
                    });
                    
                    if (cancelAddBtn) {
                        cancelAddBtn.addEventListener('click', function() {
                            addForm.style.display = 'none';
                            showAddBtn.style.display = 'block';
                            // 清空表单字段
                            const inputs = addForm.querySelectorAll('input[type="text"], input[type="number"]');
                            inputs.forEach(input => input.value = '');
                        });
                    }
                }
                
                if (showUpdateBtn && updateForm) {
                    showUpdateBtn.addEventListener('click', function() {
                        updateForm.style.display = 'block';
                        addForm.style.display = 'none';
                        showUpdateBtn.style.display = 'none';
                        showAddBtn.style.display = 'block';
                    });
                    
                    if (cancelUpdateBtn) {
                        cancelUpdateBtn.addEventListener('click', function() {
                            updateForm.style.display = 'none';
                            showUpdateBtn.style.display = 'block';
                            // 清空表单字段
                            const inputs = updateForm.querySelectorAll('input[type="text"], input[type="number"]');
                            inputs.forEach(input => input.value = '');
                        });
                    }
                }
            }
        });
    </script>
</body>
</html>