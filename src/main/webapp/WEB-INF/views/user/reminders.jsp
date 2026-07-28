<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Reminders - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3"><i class="bi bi-alarm text-primary"></i> Medicine Reminders</h4>

  <div class="card mg-glass p-3 mb-4">
    <form method="post" action="${pageContext.request.contextPath}/user/reminders" class="row g-2 align-items-end">
      <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
      <div class="col-md-4"><label class="form-label small">Medicine</label>
        <select name="medicineId" class="form-select form-select-sm" required>
          <option value="">Choose...</option>
          <c:forEach var="am" items="${allMeds}"><option value="${am.medicineId}">${am.medicineName}</option></c:forEach>
        </select></div>
      <div class="col-md-2"><label class="form-label small">Frequency</label>
        <select name="frequency" class="form-select form-select-sm">
          <option>DAILY</option><option>WEEKLY</option><option>MONTHLY</option>
        </select></div>
      <div class="col-md-2"><label class="form-label small">Time</label>
        <input name="remindTime" type="time" class="form-control form-control-sm" required></div>
      <div class="col-md-2"><label class="form-label small">Detail <span class="text-muted">(optional)</span></label>
        <input name="detail" class="form-control form-control-sm" placeholder="e.g. Monday / Day 15"></div>
      <div class="col-md-2"><button class="btn btn-primary btn-sm w-100">Add reminder</button></div>
    </form>
  </div>

  <c:if test="${empty list}"><div class="alert alert-info">No reminders yet.</div></c:if>
  <c:if test="${not empty list}">
    <div class="table-responsive card mg-glass p-2">
      <table class="table align-middle mb-0">
        <thead><tr><th>Medicine</th><th>Frequency</th><th>Time</th><th>Detail</th><th>Status</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${list}">
          <tr class="${r.active ? '' : 'opacity-50'}">
            <td class="fw-bold">${r.medicineName}</td>
            <td><span class="badge text-bg-light">${r.frequency}</span></td>
            <td>${r.remindTime}</td>
            <td class="small text-muted">${r.detail}</td>
            <td>
              <span class="badge ${r.active ? 'text-bg-success' : 'text-bg-secondary'}">
                ${r.active ? 'Active' : 'Paused'}</span>
            </td>
            <td class="text-end">
              <form method="post" action="${pageContext.request.contextPath}/user/reminders" class="d-inline">
                <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
                <input type="hidden" name="action" value="toggle">
                <input type="hidden" name="id" value="${r.reminderId}">
                <button class="btn btn-sm btn-outline-secondary" title="Pause / resume">
                  <i class="bi ${r.active ? 'bi-pause' : 'bi-play'}"></i></button>
              </form>
              <form method="post" action="${pageContext.request.contextPath}/user/reminders" class="d-inline">
                <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${r.reminderId}">
                <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
