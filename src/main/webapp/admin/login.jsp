<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Admin Login - MedGuard AI</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body class="mg-auth-bg-admin d-flex flex-column min-vh-100">
<div class="container my-auto py-5" style="max-width:430px">
  <div class="card mg-glass p-4 shadow">
    <h4 class="text-center mb-3"><i class="bi bi-shield-lock text-danger"></i> Admin Login</h4>

    <c:if test="${param.error == '1'}">
      <div class="alert alert-danger py-2">Invalid admin credentials.</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/adminlogin" class="needs-validation" novalidate>
      <div class="mb-3">
        <label class="form-label">Username</label>
        <input type="text" name="username" class="form-control" required>
        <div class="invalid-feedback">Enter the admin username.</div>
      </div>
      <div class="mb-3">
        <label class="form-label">Password</label>
        <input type="password" name="password" class="form-control" required>
        <div class="invalid-feedback">Enter the password.</div>
      </div>
      <button class="btn btn-danger w-100">Login</button>
    </form>
    <div class="text-center mt-3 small"><a href="${pageContext.request.contextPath}/index.jsp">Back to site</a></div>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
