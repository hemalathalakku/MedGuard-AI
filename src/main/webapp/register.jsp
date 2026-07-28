<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Register - MedGuard AI</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body class="mg-auth-bg d-flex flex-column min-vh-100">
<div class="container my-auto py-5" style="max-width:430px">
  <div class="card mg-glass p-4 shadow">
    <h4 class="text-center mb-3"><i class="bi bi-person-plus text-success"></i> Create your account</h4>

    <c:if test="${param.error == 'exists'}">
      <div class="alert alert-danger py-2">That email is already registered.</div>
    </c:if>
    <c:if test="${param.error == 'invalid'}">
      <div class="alert alert-danger py-2">Please fill every field correctly (password: minimum 8 characters).</div>
    </c:if>

    <form method="post" action="register" class="needs-validation" novalidate>
      <div class="mb-3">
        <label class="form-label">Full name</label>
        <input type="text" name="name" class="form-control" required>
        <div class="invalid-feedback">Enter your name.</div>
      </div>
      <div class="mb-3">
        <label class="form-label">Email</label>
        <input type="email" name="email" class="form-control" required>
        <div class="invalid-feedback">Enter a valid email.</div>
      </div>
      <div class="mb-3">
        <label class="form-label">Password <span class="text-muted small">(min 8 characters)</span></label>
        <input type="password" name="password" class="form-control" minlength="8" required>
        <div class="invalid-feedback">Minimum 8 characters.</div>
      </div>
      <button class="btn btn-success w-100">Register</button>
    </form>
    <div class="text-center mt-3 small"><a href="login.jsp">Already have an account? Login</a></div>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
