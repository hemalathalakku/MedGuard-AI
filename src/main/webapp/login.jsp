<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Login - MedGuard AI</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body class="mg-auth-bg d-flex flex-column min-vh-100">
<div class="container my-auto py-5" style="max-width:430px">
  <div class="card mg-glass p-4 shadow">
    <h4 class="text-center mb-3"><i class="bi bi-shield-plus text-primary"></i> Login to MedGuard AI</h4>

    <c:if test="${param.error == '1'}">
      <div class="alert alert-danger py-2">Invalid email or password.</div>
    </c:if>
    <c:if test="${param.registered == '1'}">
      <div class="alert alert-success py-2">Registration successful - please log in.</div>
    </c:if>
    <c:if test="${param.reset == '1'}">
      <div class="alert alert-success py-2">Password changed - log in with your new password.</div>
    </c:if>

    <%-- Public form: session doesn't exist yet, so no CSRF token here by design. --%>
    <form method="post" action="login" class="needs-validation" novalidate>
      <div class="mb-3">
        <label class="form-label">Email</label>
        <input type="email" name="email" class="form-control" required>
        <div class="invalid-feedback">Enter your email.</div>
      </div>
      <div class="mb-3">
        <label class="form-label">Password</label>
        <input type="password" name="password" class="form-control" required>
        <div class="invalid-feedback">Enter your password.</div>
      </div>
      <button class="btn btn-primary w-100">Login</button>
    </form>
    <div class="d-flex justify-content-between mt-3 small">
      <a href="forgot.jsp">Forgot password?</a>
      <a href="register.jsp">Create account</a>
    </div>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
