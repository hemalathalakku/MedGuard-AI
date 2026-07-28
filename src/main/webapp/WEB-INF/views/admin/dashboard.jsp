<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Admin Dashboard - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/adminNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3">Admin Dashboard</h4>
  <div class="row g-3">
    <div class="col-md-4">
      <div class="card mg-widget p-4 text-center">
        <i class="bi bi-people display-5 text-primary"></i>
        <div class="fs-2 fw-bold">${totalUsers}</div><div class="text-muted">Registered users</div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card mg-widget p-4 text-center">
        <i class="bi bi-capsule display-5 text-success"></i>
        <div class="fs-2 fw-bold">${totalMedicines}</div><div class="text-muted">Medicines in catalogue</div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card mg-widget p-4 text-center">
        <i class="bi bi-shield-check display-5 text-warning"></i>
        <div class="fs-2 fw-bold">${totalChecks}</div><div class="text-muted">Safety checks run</div>
      </div>
    </div>
  </div>
  <div class="mt-4">
    <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-outline-primary">
      <i class="bi bi-bar-chart"></i> Open reports &amp; charts</a>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
