<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>Drug Interactions - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/adminNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3">Drug-Drug Interaction Rules</h4>

  <div class="card mg-glass p-3 mb-4">
    <form method="post" action="${pageContext.request.contextPath}/admin/interactions" class="row g-2">
      <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
      <div class="col-md-3">
        <select name="medicineA" class="form-select form-select-sm" required>
          <option value="">Medicine A...</option>
          <c:forEach var="m" items="${allMeds}"><option value="${m.medicineId}">${m.medicineName}</option></c:forEach>
        </select></div>
      <div class="col-md-3">
        <select name="medicineB" class="form-select form-select-sm" required>
          <option value="">Medicine B...</option>
          <c:forEach var="m" items="${allMeds}"><option value="${m.medicineId}">${m.medicineName}</option></c:forEach>
        </select></div>
      <div class="col-md-2">
        <select name="risk" class="form-select form-select-sm">
          <option>LOW</option><option>MODERATE</option><option>HIGH</option>
        </select></div>
      <div class="col-md-3"><input name="reason" class="form-control form-control-sm" placeholder="Why is this risky?" required></div>
      <div class="col-md-1"><button class="btn btn-danger btn-sm w-100">Add</button></div>
    </form>
    <div class="small text-muted mt-1">Rules apply in both directions - one entry covers A+B and B+A.</div>
  </div>

  <div class="table-responsive card mg-glass p-2">
    <table class="table align-middle mb-0">
      <thead><tr><th>Medicine A</th><th>Medicine B</th><th>Risk</th><th>Reason</th><th></th></tr></thead>
      <tbody>
      <c:forEach var="r" items="${rules}">
        <tr>
          <td class="fw-bold">${r.medA}</td><td class="fw-bold">${r.medB}</td>
          <td><span class="badge ${r.risk == 'HIGH' ? 'text-bg-danger' : r.risk == 'MODERATE' ? 'text-bg-warning' : 'text-bg-secondary'}">${r.risk}</span></td>
          <td class="small">${r.reason}</td>
          <td class="text-end">
            <form method="post" action="${pageContext.request.contextPath}/admin/interactions" class="d-inline">
              <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="${r.id}">
              <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
</body>
</html>
