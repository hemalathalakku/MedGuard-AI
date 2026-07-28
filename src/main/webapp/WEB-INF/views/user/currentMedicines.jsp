<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><title>My Medicines - MedGuard AI</title><%@ include file="/WEB-INF/views/fragments/head.jspf" %></head>
<body class="d-flex flex-column min-vh-100 mg-body">
<%@ include file="/WEB-INF/views/fragments/userNav.jspf" %>
<div class="container py-4">
  <h4 class="mb-3"><i class="bi bi-capsule text-success"></i> Medicines I currently take</h4>

  <div class="card mg-glass p-3 mb-4">
    <form method="post" action="${pageContext.request.contextPath}/user/current" class="row g-2 align-items-end">
      <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
      <div class="col-md-3"><label class="form-label small">Medicine</label>
        <select name="medicineId" class="form-select form-select-sm" required>
          <option value="">Choose...</option>
          <c:forEach var="am" items="${allMeds}"><option value="${am.medicineId}">${am.medicineName}</option></c:forEach>
        </select></div>
      <div class="col-md-2"><label class="form-label small">Dose</label>
        <input name="dose" class="form-control form-control-sm" placeholder="e.g. 500 mg"></div>
      <div class="col-md-2"><label class="form-label small">Frequency</label>
        <input name="frequency" class="form-control form-control-sm" placeholder="e.g. Twice daily"></div>
      <div class="col-md-1"><label class="form-label small">Days</label>
        <input name="durationDays" type="number" min="1" class="form-control form-control-sm"></div>
      <div class="col-md-2"><label class="form-label small">Doctor</label>
        <input name="doctorName" class="form-control form-control-sm"></div>
      <div class="col-md-2"><label class="form-label small">Started on</label>
        <input name="startedOn" type="date" class="form-control form-control-sm"></div>
      <div class="col-md-10"><label class="form-label small">Notes</label>
        <input name="notes" class="form-control form-control-sm"></div>
      <div class="col-md-2"><button class="btn btn-success btn-sm w-100">Add medicine</button></div>
    </form>
  </div>

  <c:if test="${empty list}"><div class="alert alert-info">No current medicines recorded yet.</div></c:if>
  <c:if test="${not empty list}">
    <div class="table-responsive card mg-glass p-2">
      <table class="table align-middle mb-0">
        <thead><tr><th>Medicine</th><th>Dose</th><th>Frequency</th><th>Duration</th><th>Doctor</th><th>Notes</th><th>Started</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="cm" items="${list}">
          <tr>
            <td class="fw-bold">${cm.medicineName}</td>
            <td>${cm.dose}</td><td>${cm.frequency}</td>
            <td>${cm.durationDays} days</td>
            <td>${cm.doctorName}</td><td class="small text-muted">${cm.notes}</td>
            <td class="small">${cm.startedOn}</td>
            <td class="text-end">
              <form method="post" action="${pageContext.request.contextPath}/user/current" class="d-inline"
                    onsubmit="return confirm('Remove ${cm.medicineName}?')">
                <input type="hidden" name="csrf" value="${sessionScope.csrfToken}">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${cm.id}">
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
