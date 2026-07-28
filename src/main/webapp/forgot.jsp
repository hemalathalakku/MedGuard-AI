<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Forgot Password - MedGuard AI</title>
  <%@ include file="/WEB-INF/views/fragments/head.jspf" %>
</head>
<body class="mg-auth-bg d-flex flex-column min-vh-100">
<div class="container my-auto py-5" style="max-width:430px">
  <div class="card mg-glass p-4 shadow">
    <h4 class="text-center mb-3"><i class="bi bi-key text-warning"></i> Reset your password</h4>

    <c:if test="${param.error == 'noaccount'}">
      <div class="alert alert-danger py-2">No account found for that email.</div>
    </c:if>
    <c:if test="${param.error == 'badtoken'}">
      <div class="alert alert-danger py-2">Invalid or expired reset token.</div>
    </c:if>

    <c:choose>
      <c:when test="${not empty param.token}">
        <%-- Step 2: token issued. In production this form would be reached
             from an emailed link; here the token arrives via redirect. --%>
        <div class="alert alert-info py-2 small">
          Reset link generated. (Educational build: shown here instead of being emailed.)
        </div>
        <form method="post" action="forgot" class="needs-validation" novalidate>
          <input type="hidden" name="action" value="reset">
          <input type="hidden" name="token" value="${param.token}">
          <div class="mb-3">
            <label class="form-label">New password <span class="text-muted small">(min 8 characters)</span></label>
            <input type="password" name="password" class="form-control" minlength="8" required>
            <div class="invalid-feedback">Minimum 8 characters.</div>
          </div>
          <button class="btn btn-warning w-100">Set new password</button>
        </form>
      </c:when>
      <c:otherwise>
        <%-- Step 1: request a token --%>
        <form method="post" action="forgot" class="needs-validation" novalidate>
          <input type="hidden" name="action" value="request">
          <div class="mb-3">
            <label class="form-label">Account email</label>
            <input type="email" name="email" class="form-control" required>
            <div class="invalid-feedback">Enter your account email.</div>
          </div>
          <button class="btn btn-warning w-100">Generate reset link</button>
        </form>
      </c:otherwise>
    </c:choose>
    <div class="text-center mt-3 small"><a href="login.jsp">Back to login</a></div>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
